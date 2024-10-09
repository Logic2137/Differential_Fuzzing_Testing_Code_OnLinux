import java.lang.annotation.Repeatable;

@Ann(1)
@Ann(2)
class C {
}

@Repeatable(AnnContainer.class)
@interface Ann {

    int value();
}

@interface AnnContainer {

    Ann[] value();
}
