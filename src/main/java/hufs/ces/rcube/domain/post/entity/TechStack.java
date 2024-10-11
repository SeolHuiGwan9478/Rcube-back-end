package hufs.ces.rcube.domain.post.entity;

public enum TechStack {
    AWS,
    SPRING,
    JAVA,
    PYTHON,
    C,
    JAVASCRIPT;

    // 문자열로 받은 값을 유효한지 확인하는 메서드
    public static boolean isValid(String value) {
        for (TechStack tech : TechStack.values()) {
            if (tech.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}

