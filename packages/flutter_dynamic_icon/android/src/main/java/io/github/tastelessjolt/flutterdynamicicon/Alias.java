package io.github.tastelessjolt.flutterdynamicicon;

public class Alias {
    final String className;
    final String title;
    final int iconResId;

    Alias(String className, String title, int iconResId) {
        this.className = className;
        this.title = title;
        this.iconResId = iconResId;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return className.equals(((Alias) o).className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}