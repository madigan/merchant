package tech.otter.merchant.model;

public class NPC {
    private String name;
    private String portrait;

    public NPC(String name, String portrait) {
        this.name = name;
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public NPC setName(String name) {
        this.name = name;
        return this;
    }

    public String getPortrait() {
        return portrait;
    }

    public NPC setPortrait(String portrait) {
        this.portrait = portrait;
        return this;
    }
}
