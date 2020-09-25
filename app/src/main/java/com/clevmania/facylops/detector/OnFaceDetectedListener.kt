package com.clevmania.facylops.detector

/**
 * @author by Lawrence on 9/25/20.
 * for Facylops
 */
interface OnFaceDetectedListener {
    fun onSuccess(faceBounds: List<FaceBounds>) {}
    fun onFailure(exception: Exception) {}
}