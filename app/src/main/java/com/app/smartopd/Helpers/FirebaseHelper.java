package com.app.smartopd.Helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * ══════════════════════════════════════════════
 *  FirebaseHelper.java
 *  Central class for all Firebase DB references
 *  Use: FirebaseHelper.getDoctorRef("doctor_001")
 * ══════════════════════════════════════════════
 *
 * DATABASE STRUCTURE:
 *  SmartOPD/
 *  ├── doctors/{doctorId}/
 *  │     ├── name
 *  │     ├── specialization
 *  │     ├── isAvailable   → boolean
 *  │     ├── sessionFull   → boolean
 *  │     ├── emergencyLeave→ boolean
 *  │     ├── maxTokens     → int
 *  │     ├── currentToken  → int
 *  │     ├── timeSlot      → "Morning" | "Evening"
 *  │     └── plannedLeave  → "DD/MM/YYYY"
 *  │
 *  └── tokens/{tokenId}/
 *        ├── patientName
 *        ├── phone
 *        ├── doctorId
 *        ├── tokenNumber   → String
 *        ├── status        → "waiting"|"serving"|"done"|"cancelled"
 *        ├── timeSlot      → "Morning" | "Evening"
 *        └── timestamp     → long
 */
public class FirebaseHelper {

    // ── Root Database Reference ───────────────────
    private static DatabaseReference rootRef;

    // Returns root reference (singleton)
    public static DatabaseReference getRootRef() {
        if (rootRef == null) {
            rootRef = FirebaseDatabase.getInstance().getReference();
        }
        return rootRef;
    }

    // ── Doctor References ─────────────────────────

    // All doctors node
    public static DatabaseReference getAllDoctorsRef() {
        return getRootRef().child("doctors");
    }

    // Specific doctor node — use this in all 3 fragments
    public static DatabaseReference getDoctorRef(String doctorId) {
        return getRootRef().child("doctors").child(doctorId);
    }

    // Specific doctor field — e.g. getDoctorField("doctor_001", "isAvailable")
    public static DatabaseReference getDoctorField(String doctorId, String field) {
        return getDoctorRef(doctorId).child(field);
    }

    // ── Token References ──────────────────────────

    // All tokens node
    public static DatabaseReference getAllTokensRef() {
        return getRootRef().child("tokens");
    }

    // Specific token node
    public static DatabaseReference getTokenRef(String tokenId) {
        return getRootRef().child("tokens").child(tokenId);
    }

    // ── Quick-access field methods ────────────────
    // Use these directly in your fragments instead of chaining

    // Toggle availability
    public static DatabaseReference getAvailabilityRef(String doctorId) {
        return getDoctorField(doctorId, "isAvailable");
    }

    // Mark session full
    public static DatabaseReference getSessionFullRef(String doctorId) {
        return getDoctorField(doctorId, "sessionFull");
    }

    // Max tokens per session
    public static DatabaseReference getMaxTokensRef(String doctorId) {
        return getDoctorField(doctorId, "maxTokens");
    }

    // Current token count (how many issued today)
    public static DatabaseReference getCurrentTokenRef(String doctorId) {
        return getDoctorField(doctorId, "currentToken");
    }

    // Time slot (Morning / Evening)
    public static DatabaseReference getTimeSlotRef(String doctorId) {
        return getDoctorField(doctorId, "timeSlot");
    }

    // Planned leave date
    public static DatabaseReference getPlannedLeaveRef(String doctorId) {
        return getDoctorField(doctorId, "plannedLeave");
    }

    // Emergency leave flag
    public static DatabaseReference getEmergencyLeaveRef(String doctorId) {
        return getDoctorField(doctorId, "emergencyLeave");
    }
}

