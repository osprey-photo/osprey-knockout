module osprey {
    requires javafx.controls;
    requires java.logging;
    requires java.desktop;
    requires transitive javafx.graphics;

    exports osprey.competition;
    exports osprey.competition.knockout;


}