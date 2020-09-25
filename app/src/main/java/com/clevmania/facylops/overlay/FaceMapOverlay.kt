package com.clevmania.facylops.overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.clevmania.facylops.R
import com.clevmania.facylops.detector.FaceBounds
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour

/**
 * @author by Lawrence on 9/25/20.
 * for Facylops
 */
class FaceMapOverlay@JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) :
    View(ctx, attrs) {

    private val facesBounds = mutableListOf<FaceBounds>()
    private val allFaces = mutableListOf<Face>()
    private val anchorPaint = Paint()
    private val idPaint = Paint()
    private val boundsPaint = Paint()
    private val facePositionPaint: Paint

    init {
        anchorPaint.color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)

        idPaint.color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
        idPaint.textSize = 40f

        facePositionPaint = Paint()
        facePositionPaint.color = Color.GREEN

        boundsPaint.style = Paint.Style.STROKE
        boundsPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        boundsPaint.strokeWidth = 4f
    }

    internal fun updateFaces(bounds: List<FaceBounds>, faces: List<Face>) {
        facesBounds.clear()
        facesBounds.addAll(bounds)
        allFaces.clear()
        allFaces.addAll(faces)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        facesBounds.forEach { faceBounds ->
            canvas.drawBounds(faceBounds.box)
        }
        
        allFaces.forEach {
//            it.allContours.forEach {faceContour ->
//                faceContour.points.forEach {pointF ->
//                    canvas.drawCircle(pointF.x,pointF.y, FACE_POSITION_RADIUS,facePositionPaint)
//                }
//            }

            canvas.drawFace(FaceContour.FACE, Color.BLUE,it)

            // left eye
            canvas.drawFace(FaceContour.LEFT_EYEBROW_TOP, Color.RED,it)
            canvas.drawFace(FaceContour.LEFT_EYE, Color.BLACK,it)
            canvas.drawFace(FaceContour.LEFT_EYEBROW_BOTTOM, Color.CYAN,it)

            // right eye
            canvas.drawFace(FaceContour.RIGHT_EYE, Color.DKGRAY,it)
            canvas.drawFace(FaceContour.RIGHT_EYEBROW_BOTTOM, Color.GRAY,it)
            canvas.drawFace(FaceContour.RIGHT_EYEBROW_TOP, Color.GREEN,it)

            // nose
            canvas.drawFace(FaceContour.NOSE_BOTTOM, Color.LTGRAY,it)
            canvas.drawFace(FaceContour.NOSE_BRIDGE, Color.MAGENTA,it)

            // lips
            canvas.drawFace(FaceContour.LOWER_LIP_BOTTOM, Color.WHITE,it)
            canvas.drawFace(FaceContour.LOWER_LIP_TOP, Color.YELLOW,it)
            canvas.drawFace(FaceContour.UPPER_LIP_BOTTOM, Color.GREEN,it)
            canvas.drawFace(FaceContour.UPPER_LIP_TOP, Color.CYAN,it)
        }
        
    }

    /** Draws an anchor (dot) at the center of a face. */
    private fun Canvas.drawAnchor(center: PointF) {
        drawCircle(center.x, center.y,
            ANCHOR_RADIUS, anchorPaint)
    }

    /** Draws (Writes) the face's id. */
    private fun Canvas.drawId(faceId: String, center: PointF) {
        drawText("face id $faceId", center.x - ID_OFFSET, center.y + ID_OFFSET, idPaint)
    }

    /** Draws bounds around a face as a rectangle. */
    private fun Canvas.drawBounds(box: RectF) {
        drawRect(box, boundsPaint)
    }

    private fun Canvas.drawFace(position : Int, @ColorInt contourColor : Int, face: Face){
        val contour = face.getContour(position)
        val path = Path()

        contour?.points?.forEachIndexed { index, pointF ->
            if (index == 0) {
                path.moveTo((pointF.x), (pointF.y))
            }
            path.lineTo((pointF.x), (pointF.y))
        }
        val paint = Paint().apply {
            color = contourColor
            style = Paint.Style.STROKE
            strokeWidth = BOX_STROKE_WIDTH
        }
        drawPath(path, paint)
    }

    private fun RectF.center(): PointF {
        val centerX = left + (right - left) / 2
        val centerY = top + (bottom - top) / 2
        return PointF(centerX, centerY)
    }

    companion object {
        private const val ANCHOR_RADIUS = 10f
        private const val ID_OFFSET = 50f
        private const val BOX_STROKE_WIDTH = 5.0f
        private const val FACE_POSITION_RADIUS = 4.0f
    }
}