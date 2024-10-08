package tomrowicki.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {

//FIXME
/*
Gabe, what screen aspect ratio are you normally working with?
I noticed that square sprites (16x16) appear taller than wide on my screen.
I didn't notice so much working with the mario blocks, but it became really obvious when I added a couple of large images to my scene.
I realized today that the problem is the camera's projection aspect didn't match mys creen's aspect.
Camera is set to 6x3, but I have a 1920/1080 screen.
I fixed the issue by setting the camera's projectionHeight to projectWidth/screenAspect.
(I added an easy way to retrieve the screen aspect from the window)
Throwing this outt here for anyone else building along as this same issue plagued my last engine and I corrected for it in all the wrong places,
not realizing it was due to the projection.
*/

    /*
     @Murderface666  That sounds like it might be an issue with the projection matrix in your camera.
      I did do some refactoring and I changed the engine to use world units instead of pixel units,
       since Box2D operates using world units like meters.
        So you may have to change your scales on your objects to like 0.25 to get a properly sized object
     */


    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;

    private float projectionWidth = 6;
    private float projectionHeight = 3;

    public Vector4f clearColor = new Vector4f(1, 1, 1, 1);
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
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom,
                0.0f, projectionSize.y * zoom, 0.0f, 100.0f);
        inverseProjection = new Matrix4f(projectionMatrix).invert();
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        inverseView = new Matrix4f(this.viewMatrix).invert();

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public Vector2f getProjectionSize() {
        return this.projectionSize;
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