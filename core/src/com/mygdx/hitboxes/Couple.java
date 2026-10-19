package com.mygdx.hitboxes;

public class Couple {
    private final Hitbox h;
    private final Collider r;

    public Couple(Hitbox hitbox, Collider collider) {
        h = hitbox;
        r = collider;
    }

    public Hitbox getHitbox() {
        return h;
    }

    public Collider getCollider() {
        return r;
    }

    public boolean contains(Hitbox hitbox) {
        return h.equals(hitbox);
    }

    public boolean contains(Collider collider) {
        return r.equals(collider);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Couple c)) return false;
        return h.equals(c.h) && r.equals(c.r);
    }

    @Override
    public int hashCode() {
        int result = h.hashCode();
        result = 31 * result + r.hashCode();
        return result;
    }
}
