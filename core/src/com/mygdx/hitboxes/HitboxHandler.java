package com.mygdx.hitboxes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class HitboxHandler {
    private static HitboxHandler instance;
    private final CopyOnWriteArrayList<Collider> colliders = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<Tags, CopyOnWriteArrayList<Hitbox>> hitboxes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Couple, Boolean> contacts = new ConcurrentHashMap<>();

    public static HitboxHandler get() {
        return instance;
    }

    public static void set(HitboxHandler h) {
        instance = h;
        instance.clearContacts();
    }

    public void registerHitbox(Hitbox h) {
        for (Tags tag : h.getTags()) {
            CopyOnWriteArrayList<Hitbox> list = hitboxes.computeIfAbsent(tag, k -> new CopyOnWriteArrayList<>());
            list.add(h);
        }
    }

    public void registerCollider(Collider r) {
        colliders.add(r);
    }

    public void unRegisterHitbox(Hitbox h) {
        for (Tags tag : h.getTags()) {
            CopyOnWriteArrayList<Hitbox> list = hitboxes.get(tag);
            if (list != null) {
                list.remove(h);
            }
        }

        for (Couple couple : contacts.keySet()) {
            if (!couple.contains(h))
                continue;
            contacts.remove(couple);
            couple.getCollider().onLeave(h);
        }
    }

    public void unRegisterCollider(Collider r) {
        colliders.remove(r);
        for (Couple couple : contacts.keySet()) {
            if (!couple.contains(r))
                continue;
            contacts.remove(couple);
            couple.getHitbox().onLeave(r);
        }
    }

    public Boolean storeContact(Collider r, Hitbox h) {
        return contacts.putIfAbsent(new Couple(h, r), false);
    }

    public boolean getContact(Collider r, Hitbox h) {
        return contacts.getOrDefault(new Couple(h, r), false);
    }

    public void setContact(Collider r, Hitbox h, boolean value) {
        if (!contacts.replace(new Couple(h, r), !value, value))
            return;

        if (value) {
            r.onHit(h);
            h.onHit(r);
        } else {
            r.onLeave(h);
            h.onLeave(r);
        }
    }

    public void removeContact(Collider r, Hitbox h) {
        if (contacts.remove(new Couple(h, r)) == null)
            return;

        r.onLeave(h);
        h.onLeave(r);
    }

    public boolean checkHitbox(Collider r, Hitbox h, boolean activate) {
        return h.isHit(r, activate);
    }

    public void checkRegistered() {
        for (Collider r : colliders) {
            checkDefault(r, true);
        }
    }

    public boolean checkAll(Collider r, boolean activate) {
        boolean hit = false;
        for (CopyOnWriteArrayList<Hitbox> list : hitboxes.values()) {
            for (Hitbox h : list) {
                hit = h.isHit(r, activate) || hit;
            }
        }
        return hit;
    }

    public boolean checkWith(Collider c, boolean activate, Tags... searchTags) {
        boolean hit = false;
        for (Tags tag : searchTags) {
            CopyOnWriteArrayList<Hitbox> list = hitboxes.get(tag);
            if (list == null)
                continue;
            for (Hitbox h : list) {
                hit = h.isHit(c, activate) || hit;
            }
        }
        return hit;
    }

    public boolean checkDefault(Collider r, boolean activate) {
        if (r.containsSearchTag(Tags.ALL)) {
            return checkAll(r, activate);
        }
        Tags[] tagsArr = new Tags[r.getSearchTags().size()];
        r.getSearchTags().toArray(tagsArr);
        return checkWith(r, activate, tagsArr);
    }

    public boolean checkWithOffset(Collider r, boolean activate, float x, float y, Tags... searchTags) {
        boolean hit;
        r.translate(x, y);
        if (searchTags[0].equals(Tags.ALL)) {
            hit = checkAll(r, activate);
        } else
            hit = checkWith(r, activate, searchTags);
        r.translate(-x, -y);
        return hit;
    }

    public void clearContacts() {
        contacts.clear();
    }
}