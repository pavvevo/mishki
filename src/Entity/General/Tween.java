package Entity.General;

import java.util.function.Consumer;

public class Tween {

    private double from;
    private double to;
    public int frame;
    private int end_frame;
    private String type;
    private Consumer<Double> onUpdate;
    private Runnable onComplete;
    public boolean is_finished;

    public String name;

    public Tween(double from, double to, int frames, String type, String name, Consumer<Double> onUpdate, Runnable onComplete) {
        this.from = from;
        this.to = to;
        this.frame = 0;
        this.end_frame = frames;
        this.type = type;
        this.onUpdate = onUpdate;
        this.onComplete = onComplete;
        this.name = name;
        this.is_finished = false;
    }

    private double lerp(double a, double b, double f) {
        return (a * (1.0 - f)) + (b * f);
    }

    public void update() {
        if (is_finished) return;

        frame += 1;
        if (frame > end_frame) {
            frame = end_frame;
            is_finished = true;
        }

        double progress = (double) frame / end_frame;
        double eased_progress = applyEasing(progress);
        double current_value = lerp(from, to, eased_progress);

        if (onUpdate != null) onUpdate.accept(current_value);
        if (is_finished && onComplete != null) onComplete.run();
    }

    private double applyEasing(double progress) {
        switch (type) {
            default:
                return progress;
            case "Ease In":
                return Math.pow(progress, 3);
            case "Ease Out":
                return 1.0 - Math.pow(1 - progress, 3);
        }
    }
}