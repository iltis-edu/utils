package de.tudortmund.cs.iltis.utils.collections;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * A simple implementation of an Either type, known from functional programming. Each object of this
 * class can either be a <code>left a</code> or a <code>right b</code> where <code>a</code> is of
 * type <code>A</code> and <code>b</code> is of type <code>B</code>.
 *
 * @param <A> the type of left elements
 * @param <B> the type of right elements
 */
public abstract class Either<A, B> implements Serializable {
    public boolean isLeft() {
        return false;
    }

    public boolean isRight() {
        return false;
    }

    public A getLeft() {
        throw new NoSuchElementException();
    }

    public B getRight() {
        throw new NoSuchElementException();
    }

    public static <A, B> Either<A, B> left(A a) {
        return new Left<>(a);
    }

    public static <A, B> Either<A, B> right(B b) {
        return new Right<>(b);
    }

    /** for GWT serialization */
    private Either() {}

    private static final long serialVersionUID = 1L;

    public static class Left<A, B> extends Either<A, B> {
        private A content;

        protected Left(A a) {
            content = a;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public A getLeft() {
            return content;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((content == null) ? 0 : content.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof Left)) return false;
            Left<A, B> other = (Left<A, B>) obj;
            if (content == null) return other.content == null;
            return content.equals(other.content);
        }

        @Override
        public String toString() {
            return "Left: " + content.toString();
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private Left() {}

        private static final long serialVersionUID = 1L;
    }

    public static class Right<A, B> extends Either<A, B> {
        private B content;

        protected Right(B b) {
            content = b;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public B getRight() {
            return content;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((content == null) ? 0 : content.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof Right)) return false;
            Right<A, B> other = (Right<A, B>) obj;
            if (content == null) return other.content == null;
            return content.equals(other.content);
        }

        @Override
        public String toString() {
            return "Right: " + content.toString();
        }

        /** for GWT serialization */
        @SuppressWarnings("unused")
        private Right() {}

        private static final long serialVersionUID = 1L;
    }
}
