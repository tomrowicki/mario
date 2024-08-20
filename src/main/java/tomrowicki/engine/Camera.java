package tomrowicki.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    // FIXME
    /*
    Gabe, what screen aspect ratio are you normally working with?
    I noticed that square sprites (16x16) appear taller than wide on my screen.
    I didn't notice so much working with the mario blocks, but it became really obvious when I added a couple of large images to my scene.
    I realized today that the problem is the camera's projection aspect didn't match my screen's aspect.
    Camera is set to 6 x 3, but I have a 1920/1080 screen.
    I fixed the issue by setting the camera's projectionHeight to projectWidth/screenAspect.
    (I added an easy way to retrieve the screen aspect from the window)
    Throwing this out there for anyone else building along as this same issue plagued my last engine and I corrected for it in all the wrong places,
     not realizing it was due to the projection.
     */

    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView; // 4x4 matrices
    public Vector2f position;

    private float projectionWidth = 6;
    private float projectionHeight = 3;
    private Vector2f projectionSize = new Vector2f(projectionWidth, projectionHeight);

    private float zoom = 1.0f;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity(); // see: identity matrix (whatever is multiplied by this gives itself as the result)
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f,
                projectionSize.y * this.zoom, 0.0f, 100.0f); // splits everything into tiles
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        this.viewMatrix.invert(inverseView);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void addZoom(float value) {
        this.zoom += value;
    }
}
