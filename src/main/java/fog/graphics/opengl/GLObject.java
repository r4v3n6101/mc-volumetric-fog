package fog.graphics.opengl;

import fog.utils.Cleanable;

public interface GLObject extends Cleanable {

    void bind();

    int getId();
}
