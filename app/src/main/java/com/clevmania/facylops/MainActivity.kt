package com.clevmania.facylops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.Menu
import android.view.MenuItem
import com.clevmania.facylops.detector.FaceDetector
import com.clevmania.facylops.overlay.Frame
import com.clevmania.facylops.overlay.LensFacing
import com.otaliastudios.cameraview.Facing
import kotlinx.android.synthetic.main.activity_main.*
const val TAG = "MainActivity"
const val LENS_FACING_EXTRA = "lens-facing"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lensFacing = savedInstanceState?.
            getSerializable(LENS_FACING_EXTRA) as Facing? ?: Facing.BACK
        setupCamera(lensFacing)
    }

    private fun setupCamera(lensFacing: Facing) {
        val faceDetector = FaceDetector(fmBoundsOverlay)
        viewfinder.facing = lensFacing
        viewfinder.addFrameProcessor {
            val frame = Frame(it.data,it.rotation,
                Size(it.size.width, it.size.height),it.format,
                if (viewfinder.facing == Facing.BACK) LensFacing.BACK else LensFacing.FRONT)
            faceDetector.process(frame)
        }
    }

    override fun onResume() {
        super.onResume()
        viewfinder.start()
    }

    override fun onPause() {
        super.onPause()
        viewfinder.stop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(LENS_FACING_EXTRA, viewfinder.facing)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewfinder.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_flip -> {
            viewfinder.toggleFacing()
            true
        }
        else -> false
    }
}