package eu.revamp.hcf.scoreboard;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScoreboardInput {
    private String prefix;
    private String name;
    private String suffix;
    private int position;
    
    public ScoreboardInput(String prefix, String name, String suffix, int position) {
        setPrefix(prefix);
        setName(name);
        setSuffix(suffix);
        setPosition(position);
    }
}
