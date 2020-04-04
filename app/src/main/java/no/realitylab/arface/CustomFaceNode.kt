package no.realitylab.arface

import android.content.Context
import android.widget.ImageView
import com.google.ar.core.AugmentedFace
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.AugmentedFaceNode

class CustomFaceNode(augmentedFace: AugmentedFace?,
                 val context: Context
): AugmentedFaceNode(augmentedFace) {

    private var eyeNodeLeft: Node? = null
    private var eyeNodeRight: Node? = null
    private var mustacheNode: Node? = null

    companion object {
        enum class FaceRegion {
            LEFT_EYE,
            RIGHT_EYE,
            MUSTACHE
        }
    }

    override fun onActivate() {
        super.onActivate()
        eyeNodeLeft = Node()
        eyeNodeLeft?.setParent(this)

        eyeNodeRight = Node()
        eyeNodeRight?.setParent(this)

        mustacheNode = Node()
        mustacheNode?.setParent(this)

        ViewRenderable.builder()
            .setView(context, R.layout.element_layout)
            .build()
            .thenAccept { uiRenderable: ViewRenderable ->
                uiRenderable.isShadowCaster = false
                uiRenderable.isShadowReceiver = false
                eyeNodeLeft?.renderable = uiRenderable
                eyeNodeRight?.renderable = uiRenderable
            }
            .exceptionally { throwable: Throwable? ->
                throw AssertionError(
                    "Could not create ui element",
                    throwable
                )
            }

        ViewRenderable.builder()
            .setView(context, R.layout.element_layout)
            .build()
            .thenAccept { uiRenderable: ViewRenderable ->
                uiRenderable.isShadowCaster = false
                uiRenderable.isShadowReceiver = false
                mustacheNode?.renderable = uiRenderable
                uiRenderable.view.findViewById<ImageView>(R.id.element_image).setImageResource(R.drawable.mustache)
            }
            .exceptionally { throwable: Throwable? ->
                throw AssertionError(
                    "Could not create ui element",
                    throwable
                )
            }
    }

    private fun getRegionPose(region: FaceRegion) : Vector3? {
        val buffer = augmentedFace?.meshVertices
        if (buffer != null) {
            return when (region) {
                FaceRegion.LEFT_EYE ->
                    Vector3(buffer.get(374 * 3),buffer.get(374 * 3 + 1),  buffer.get(374 * 3 + 2))
                FaceRegion.RIGHT_EYE ->
                    Vector3(buffer.get(145 * 3),buffer.get(145 * 3 + 1),  buffer.get(145 * 3 + 2))
                FaceRegion.MUSTACHE ->
                    Vector3(buffer.get(11 * 3),
                        buffer.get(11 * 3 + 1),
                        buffer.get(11 * 3 + 2))
            }
        }
        return null
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        augmentedFace?.let {face ->
            getRegionPose(FaceRegion.LEFT_EYE)?.let {
                eyeNodeLeft?.localPosition = Vector3(it.x, it.y - 0.035f, it.z + 0.015f)
                eyeNodeLeft?.localScale = Vector3(0.055f, 0.055f, 0.055f)
                eyeNodeLeft?.localRotation = Quaternion.axisAngle(Vector3(0.0f, 0.0f, 1.0f), -10f)
            }

            getRegionPose(FaceRegion.RIGHT_EYE)?.let {
                eyeNodeRight?.localPosition = Vector3(it.x, it.y - 0.035f, it.z + 0.015f)
                eyeNodeRight?.localScale = Vector3(0.055f, 0.055f, 0.055f)
                eyeNodeRight?.localRotation = Quaternion.axisAngle(Vector3(0.0f, 0.0f, 1.0f), 10f)
            }

            getRegionPose(FaceRegion.MUSTACHE)?.let {
                mustacheNode?.localPosition = Vector3(it.x, it.y - 0.035f, it.z + 0.015f)
                mustacheNode?.localScale = Vector3(0.07f, 0.07f, 0.07f)
            }
        }
    }
}