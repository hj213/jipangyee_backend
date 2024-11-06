package com.example.backend.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBusinessRegistration is a Querydsl query type for BusinessRegistration
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBusinessRegistration extends EntityPathBase<BusinessRegistration> {

    private static final long serialVersionUID = 484503433L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBusinessRegistration businessRegistration = new QBusinessRegistration("businessRegistration");

    public final QBaseTime _super = new QBaseTime(this);

    public final NumberPath<Long> brNum = createNumber("brNum", Long.class);

    public final QBusinessCategory businessCategory;

    public final DatePath<java.time.LocalDate> businessStartDate = createDate("businessStartDate", java.time.LocalDate.class);

    public final StringPath companyName = createString("companyName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath representativeName = createString("representativeName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBusinessRegistration(String variable) {
        this(BusinessRegistration.class, forVariable(variable), INITS);
    }

    public QBusinessRegistration(Path<? extends BusinessRegistration> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBusinessRegistration(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBusinessRegistration(PathMetadata metadata, PathInits inits) {
        this(BusinessRegistration.class, metadata, inits);
    }

    public QBusinessRegistration(Class<? extends BusinessRegistration> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.businessCategory = inits.isInitialized("businessCategory") ? new QBusinessCategory(forProperty("businessCategory")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}
