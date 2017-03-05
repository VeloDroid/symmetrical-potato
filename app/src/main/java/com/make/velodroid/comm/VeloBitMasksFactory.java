package com.make.velodroid.comm;

import com.akexorcist.googledirection.constant.Maneuver;

/**
 * Created by matgrioni on 3/5/17.
 */

public class VeloBitMasksFactory {
    private static final float DEFAULT_FAR_THRESHOLD = 80;
    private static final float DEFAULT_MID_THRESHOLD = 30;
    private static final float DEFAULT_CLO_THRESHOLD = 15;
    private static final float DEFAULT_EPI_THRESHOLD = 4;

    private float farThreshold;
    private float midThreshold;
    private float cloThreshold;
    private float epiThreshold;

    public VeloBitMasksFactory() {
        this.farThreshold = DEFAULT_FAR_THRESHOLD;
        this.midThreshold = DEFAULT_MID_THRESHOLD;
        this.cloThreshold = DEFAULT_CLO_THRESHOLD;
        this.epiThreshold = DEFAULT_EPI_THRESHOLD;
    }

    public VeloBitMasksFactory(float farThreshold, float midThreshold, float cloThreshold,
                               float epiThreshold) {
        this.farThreshold = farThreshold;
        this.midThreshold = midThreshold;
        this.cloThreshold = cloThreshold;
        this.epiThreshold = epiThreshold;
    }

    public byte createBitMask(String maneuver, float dist) {
        byte res = 0x00;

        switch (maneuver) {
            case Maneuver.TURN_RIGHT:
                res |= VeloBitMasks.TURN_RIGHT;
                break;

            case Maneuver.TURN_LEFT:
                res |= VeloBitMasks.TURN_LEFT;
                break;
        }

        if (dist < epiThreshold) {
            res |= VeloBitMasks.OFF;
            res |= VeloBitMasks.PROXIMITY_OFF;
        } else if (dist < cloThreshold) {
            res |= VeloBitMasks.ON;
            res |= VeloBitMasks.PROXIMITY_CLO;
        } else if (dist < midThreshold) {
            res |= VeloBitMasks.ON;
            res |= VeloBitMasks.PROXIMITY_MID;
        } else if (dist < farThreshold) {
            res |= VeloBitMasks.ON;
            res |= VeloBitMasks.PROXIMITY_FAR;
        } else {
            res |= VeloBitMasks.OFF;
            res |= VeloBitMasks.PROXIMITY_OFF;
        }

        return res;
    }
}
