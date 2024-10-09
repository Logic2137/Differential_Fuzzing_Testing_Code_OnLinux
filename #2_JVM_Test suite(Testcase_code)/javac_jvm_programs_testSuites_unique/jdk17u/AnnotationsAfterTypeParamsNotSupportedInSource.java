@interface Anno {
}

class AnnotationsAfterTypeParamsNotSupportedInSource {

    @Anno
    <T> int m() {
        return 0;
    }
}
