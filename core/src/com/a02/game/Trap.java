package com.a02.game;

public class Trap extends GameObject{
    private String effect;
    private float attackDamage;

    public Trap(float x, float y, int width, int height, String sprite, int id, String name, String type, int price, boolean unlocked, int hp, boolean buyable, boolean selected, String effect, float attackDamage) {
        super(x, y, width, height, sprite, id, name, type,price, unlocked, hp, buyable,selected);
        this.effect = effect;
        this.attackDamage = attackDamage;
    }

    public Trap(Trap other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(), other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp(), other.isBuyable(), other.isSelected());
        this.effect = other.getEffect();
        this.attackDamage = other.getAttackDamage();
    }

    public Trap() {
        super();
        this.effect = "";
        this.attackDamage = 0;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }
}
