package com.example.edtech.service;

import com.example.edtech.dto.CommentTreeDto;
import com.example.edtech.dto.Commentdto;
import com.example.edtech.dto.Replydto;
import com.example.edtech.entity.CommentEntity;
import com.example.edtech.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;


    public Commentdto addComment(ObjectId courseId,ObjectId userId,String content){
        CommentEntity comment = CommentEntity.toEntity( courseId, userId, content,  null,  null,0);
        CommentEntity save = commentRepository.save(comment);
        return Commentdto.fromEntity(save);
    }

    public Replydto addReply(ObjectId courseId, ObjectId parentCommentId, ObjectId userId, String content) {
        //  Find the parent comment
        CommentEntity parent = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        //  Build ancestor list: copy parent's ancestors + parent ID
        List<ObjectId> newAncestors = new ArrayList<>(parent.getAncestorIds());
        newAncestors.add(parent.getId());
        int reply=parent.getReplyCount()+1;
        //  Create reply comment
        CommentEntity comment = CommentEntity.toEntity( courseId, userId, content,  parentCommentId,  newAncestors,reply);
        CommentEntity saved = commentRepository.save(comment);
        return Replydto.fromEntity(saved);
    }

    /**
     * 3. Full tree mode (admin or small datasets)
     */
    public List<CommentTreeDto> getCommentsTree(ObjectId courseId) {
        List<CommentEntity> allComments = commentRepository.findByCourseId(courseId);
if (allComments.isEmpty())
    throw new RuntimeException("No comment is founds");
        Map<String, List<CommentTreeDto>> byParent = allComments.stream()
                .map(CommentTreeDto::fromEntity)
                .collect(Collectors.groupingBy(CommentTreeDto::getParentCommentId));

        return buildTree(null, byParent);
    }
//    grouping by works like this grouup all the commenttreeDto in map with the key as the parentid
//    {
//        null: [
//        { id: "A", parentCommentId: null, content: "First comment", replies: [] },
//        { id: "B", parentCommentId: null, content: "Second comment", replies: [] }
//  ],
//        "A": [
//        { id: "C", parentCommentId: "A", content: "Reply to A", replies: [] },
//        { id: "D", parentCommentId: "A", content: "Another reply A", replies: [] }
//  ],
//        "C": [
//        { id: "E", parentCommentId: "C", content: "Reply to C", replies: [] }
//  ]
//    }

    private List<CommentTreeDto> buildTree(String parentId, Map<String, List<CommentTreeDto>> byParent) {
        List<CommentTreeDto> children = byParent.getOrDefault(parentId, new ArrayList<>());
        for (CommentTreeDto child : children) {
            child.setReplies(buildTree(child.getId(), byParent));
        }
        return children;
    }
    /**
     * 4. YouTube-style incremental loading
     */
    public Page<Commentdto> getTopLevelComments(ObjectId courseId, int page, int size) {
        ObjectId parentId=null;
        return commentRepository
                .findByCourseIdAndParentCommentId(courseId, parentId, PageRequest.of(page, size))
                .map(Commentdto::fromEntity);
    }

    public Page<Commentdto> getReplies(ObjectId courseId, ObjectId parentId, int page, int size) {
        return commentRepository
                .findByCourseIdAndParentCommentId(courseId, parentId, PageRequest.of(page, size))
                .map(Commentdto::fromEntity);
    }

    public int  toggleLike(ObjectId commentId1, ObjectId userId) {
        CommentEntity comment = commentRepository.findById(commentId1).orElseThrow(() -> new RuntimeException("Comment not found"));
        List<ObjectId> likedByUserIds = comment.getLikedByUserIds();
    if (likedByUserIds.contains(userId))
        likedByUserIds.remove(userId);
    else
        likedByUserIds.add(userId);
commentRepository.save(comment);
    return likedByUserIds.size();
    }




//    public long countReplies(ObjectId parentId) {
//        return commentRepository.countByParentCommentId(parentId);
//    }
}

//    Your current buildTree approach
//    Loads every comment for the course in a single query.
//
//        Groups them by parentCommentId.
//
//        Recursively builds the full tree so the API response already contains all nested replies.
//
//        This is fine for small datasets (like a course with <1,000 comments) but not scalable for giant threads.

//    YouTube approach (incremental loading)
//    When you first load a video:
//
//        Only top-level comments are fetched (parentCommentId = null).
//
//        Each top-level comment has a reply count but not the replies themselves.
//
//        When you click "View replies" on a specific comment:
//
//        The client makes another request to fetch only that comment’s direct replies (and maybe their count).
//
//        If you want replies to replies, it makes another request on click.
//
//        Why?
//
//        Saves bandwidth (you’re not loading thousands of comments at once).
//
//        Speeds up initial page load.
//
//        Lets them paginate replies independently for each comment.
