package no.realitylab.arface

import android.content.Context
import android.os.Handler
import android.widget.TextView
import com.google.ar.core.AugmentedFace
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.AugmentedFaceNode

class FilterFace(augmentedFace: AugmentedFace?,
                 val context: Context): AugmentedFaceNode(augmentedFace) {

    private var cardNode: Node? = null
    private var textView: TextView? = null

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable


    val animals =  arrayOf("Dog", "Cat", "Tiger", "Frog", "Zebra", "Monkey", "Lion")

    override fun onActivate() {
        super.onActivate()
        cardNode = Node()
        cardNode?.setParent(this)

        mHandler = Handler()

        ViewRenderable.builder()
            .setView(context, R.layout.card_layout)
            .build()
            .thenAccept { uiRenderable: ViewRenderable ->
                uiRenderable.isShadowCaster = false
                uiRenderable.isShadowReceiver = false
                cardNode?.renderable = uiRenderable
                textView = uiRenderable.view.findViewById(R.id.title)
            }
            .exceptionally { throwable: Throwable? ->
                throw AssertionError(
                    "Could not create ui element",
                    throwable
                )
            }
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        augmentedFace?.let {face ->
            val rightForehead = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_RIGHT)
            val leftForehead = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_LEFT)
            val center = face.centerPose

            cardNode?.worldPosition = Vector3((leftForehead.tx() + rightForehead.tx()) / 2,
                (leftForehead.ty() + rightForehead.ty()) / 2 + 0.05f , center.tz())
        }
    }

    fun animate() {
        val index = (animals.indices).random()
        val rounds = (2..4).random()
        var currentIndex = 0
        var currentRound = 0

        mRunnable = Runnable {
            textView?.text = animals[currentIndex]
            currentIndex ++
            if (currentIndex == animals.size) {
                currentIndex = 0
                currentRound ++
            }

            if (currentRound == rounds) {
                textView?.text = animals[index]
            } else {
                // Schedule the task to repeat
                mHandler.postDelayed(
                    mRunnable, // Runnable
                    100 // Delay in milliseconds
                )
            }
        }

        // Schedule the task to repeat
        mHandler.postDelayed(
            mRunnable, // Runnable
            100 // Delay in milliseconds
        )
    }

    fun refresh() {
        textView?.text = context.getText(R.string.quiz_title)
    }
}