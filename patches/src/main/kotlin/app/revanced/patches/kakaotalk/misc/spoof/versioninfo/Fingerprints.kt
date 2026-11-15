package app.revanced.patches.kakaotalk.misc.spoof.versioninfo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.util.getReference
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.reference.StringReference

private val VERSION_STRING = Regex("^\\d+\\.\\d+\\.\\d+$")

// Matches the hardcoded app version string without pinning a specific version,
// so the fingerprints keep working across app updates.
private fun Method.hasVersionString() = implementation?.instructions?.any { instruction ->
    instruction.opcode == Opcode.CONST_STRING &&
        instruction.getReference<StringReference>()?.string?.let(VERSION_STRING::matches) == true
} == true

internal object VersionInfoFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.XOR_INT_LIT8,
        Opcode.NEW_INSTANCE,
        Opcode.INVOKE_DIRECT,
        Opcode.CONST_STRING,
        Opcode.INVOKE_VIRTUAL,
        Opcode.SGET_OBJECT,
        Opcode.RETURN_OBJECT
    ),
    custom = { method, classDef ->
        classDef.sourceFile == "AppAdminSettingsActivity.kt" && method.hasVersionString()
    }
)

internal object VersionInfoPreviewFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf(),
    returnType = "Ljava/lang/String;",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.SGET_OBJECT,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT,
        Opcode.IF_EQZ,
        Opcode.CONST_STRING,
        Opcode.RETURN_OBJECT,
    ),
    custom = { method, classDef ->
        classDef.sourceFile == "SettingActivity.kt" && method.hasVersionString()
    }
)
