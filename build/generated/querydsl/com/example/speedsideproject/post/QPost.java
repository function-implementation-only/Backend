package com.example.speedsideproject.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1533205740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.example.speedsideproject.global.QTimestamped _super = new com.example.speedsideproject.global.QTimestamped(this);

    public final com.example.speedsideproject.account.entity.QAccount account;

    public final EnumPath<com.example.speedsideproject.post.enums.Category> category = createEnum("category", com.example.speedsideproject.post.enums.Category.class);

    public final ListPath<com.example.speedsideproject.comment.entity.Comment, com.example.speedsideproject.comment.entity.QComment> comment = this.<com.example.speedsideproject.comment.entity.Comment, com.example.speedsideproject.comment.entity.QComment>createList("comment", com.example.speedsideproject.comment.entity.Comment.class, com.example.speedsideproject.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.speedsideproject.post.enums.Duration> duration = createEnum("duration", com.example.speedsideproject.post.enums.Duration.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Image, QImage> imageList = this.<Image, QImage>createList("imageList", Image.class, QImage.class, PathInits.DIRECT2);

    public final ListPath<com.example.speedsideproject.likes.Likes, com.example.speedsideproject.likes.QLikes> likes = this.<com.example.speedsideproject.likes.Likes, com.example.speedsideproject.likes.QLikes>createList("likes", com.example.speedsideproject.likes.Likes.class, com.example.speedsideproject.likes.QLikes.class, PathInits.DIRECT2);

    public final NumberPath<Long> likesLength = createNumber("likesLength", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> peopleNum = createNumber("peopleNum", Long.class);

    public final EnumPath<com.example.speedsideproject.post.enums.Place> place = createEnum("place", com.example.speedsideproject.post.enums.Place.class);

    public final StringPath startDate = createString("startDate");

    public final EnumPath<com.example.speedsideproject.post.enums.Tech> tech = createEnum("tech", com.example.speedsideproject.post.enums.Tech.class);

    public final StringPath title = createString("title");

    public final StringPath urlKey = createString("urlKey");

    public final StringPath urlToString = createString("urlToString");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.example.speedsideproject.account.entity.QAccount(forProperty("account")) : null;
    }

}

