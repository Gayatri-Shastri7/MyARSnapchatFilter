package com.example.mysnapchatfilter;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mysnapchatfilter.databinding.ActivityMainBinding;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isAdded = false;
    private AugmentedFaceNode augumentedFaceNode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomARFragment customARFragment= (CustomARFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder()
                .setSource(this,R.raw.fox_face)
                .build()
                .thenAccept(modelRenderable1 -> (
                        modelRenderable1.setShawdowCaster(false);
                        modelRenderable.setShawdowReceiver(false);
                        ));
        Texture.builder()
                .setSource(this,R.drawable.fox_face_mesh_texture)
                .build()
                .thenAccept(texture1 -> this.texture = texture);
        customARFragment.getArSceneView().getCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);
        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            if(modelRenderable==null || texture == null)
                return;
            Frame frame = customARFragment.getArSceneView().getArFrame();
            Collection<AugumentedFace> augumentedFaces = frame.getUpdatedTrackables(Config.AugmentedFaceMode(augmentedFace));
            for(AugmentedFace augmentedFace: augumentedFaces){
                if(isAdded)
                    return;
                AugmentedFace augumentedFace;
                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augumentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augumentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }

        });
    }

}