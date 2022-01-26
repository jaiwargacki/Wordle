public enum Status {
    INCORRECT("\u2B1B"),
    CORRECT("\uD83D\uDFE9"),
    PARTIAL("\uD83D\uDFE8");

    private String string;

    private Status(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
