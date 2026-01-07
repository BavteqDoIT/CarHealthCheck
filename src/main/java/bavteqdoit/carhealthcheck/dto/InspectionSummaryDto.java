package bavteqdoit.carhealthcheck.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class InspectionSummaryDto {

    private List<Message> redFlags = new ArrayList<>();
    private List<Message> yellowFlags = new ArrayList<>();
    private List<Message> greenNotes = new ArrayList<>();

    public void red(String key, Object... args) {
        redFlags.add(new Message(key, java.util.List.of(args)));
    }

    public void yellow(String key, Object... args) {
        yellowFlags.add(new Message(key, java.util.List.of(args)));
    }


    public void green(String key, Object... args) {
        greenNotes.add(new Message(key, java.util.List.of(args)));
    }

    public record Message(String key, List<Object> args) {}
}
