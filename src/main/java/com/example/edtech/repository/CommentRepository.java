package com.example.edtech.repository;

import com.example.edtech.entity.CommentEntity;
import org.apache.el.stream.Stream;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentEntity, ObjectId> {

    List<CommentEntity> findByCourseId(ObjectId courseId);


 Page<CommentEntity> findByCourseIdAndParentCommentId(ObjectId courseId, ObjectId parentId, PageRequest of);


    long countByParentCommentId(ObjectId parentId);

    void deleteByCourseId(ObjectId id);


    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'replyCount': -1 } }")
    void decrementReplyCount(ObjectId commentId);


    List<CommentEntity> findByCourseIdAndAncestorIdsContains(ObjectId courseId, ObjectId commentId);

    List<CommentEntity> findByUserId(ObjectId id);
}

//Alright — let’s unpack those three annotations one by one.
//
//---
//
//## **1. `@Modifying`**
//
//* **Purpose**: Tells Spring Data that this repository method is **not** a read query but a **write operation** (update, delete, etc.).
//* **Why needed**:
//
//  * By default, Spring Data assumes repository methods return results (i.e., `findBy...`).
//  * If you run an update or delete without `@Modifying`, Spring will try to interpret it as a `find` and fail.
//* **In our case**: We’re **updating** `replyCount` in MongoDB, so `@Modifying` is mandatory.
//
//---
//
//## **2. `@Query("{ '_id': ?0 }")`**
//
//* **Purpose**: Defines the **filter** for our MongoDB query, in raw MongoDB JSON syntax.
//* **`?0`**: This is a **parameter placeholder** for the first argument of the method.
//* Example:
//
//  ```java
//  decrementReplyCount(ObjectId commentId)
//  ```
//
//  → `?0` will be replaced with `commentId`.
//* MongoDB equivalent:
//
//  ```js
//  { _id: ObjectId("commentId_here") }
//  ```
//
//---
//
//## **3. `@Update("{ '$inc': { 'replyCount': -1 } }")`**
//
//* **Purpose**: Defines the **update document** in MongoDB syntax.
//* **`$inc`**: MongoDB operator to increment (or decrement) numeric fields.
//
//  * `$inc: { replyCount: -1 }` → subtracts **1** from `replyCount`.
//* This avoids having to load the document in Java and then save it back — it’s done **directly in the database**.
//
//---
//
//## **Putting it all together**
//
//```java
//@Modifying
//@Query("{ '_id': ?0 }")
//@Update("{ '$inc': { 'replyCount': -1 } }")
//void decrementReplyCount(ObjectId commentId);
//```
//
//This tells Spring Data:
//
//1. **`@Modifying`** → This is a write operation.
//2. **`@Query`** → Match documents with `_id = commentId`.
//3. **`@Update`** → Apply `$inc` to reduce `replyCount` by 1.
//
//**MongoDB Equivalent:**
//
//```js
//db.comments.updateOne(
//    { _id: ObjectId("commentId_here") },
//    { $inc: { replyCount: -1 } }
//);

