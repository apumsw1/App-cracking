package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppItem
import com.example.model.LogEntry
import com.example.model.SecurityCheck
import com.example.model.ToolItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AdminUiState(
    val cpuUsage: Float = 34.5f,
    val ramUsedMb: Int = 3840,
    val ramTotalMb: Int = 8192,
    val storageUsedGb: Int = 42,
    val storageTotalGb: Int = 128,
    val batteryLevel: Int = 88,
    val isRooted: Boolean = false,
    val activeConnections: Int = 14,
    
    val apps: List<AppItem> = listOf(
        AppItem("1", "Brutal Admin Loader Core", "com.example.adminloader", "4.7", false, 34, 8, "SHA256:7F8A2E..."),
        AppItem("2", "System Diagnostic Daemon", "com.android.sysdiag", "2.1", true, 34, 15, "SHA256:9B1C4F..."),
        AppItem("3", "Network Payload Analyzer", "org.net.payload", "1.8", false, 33, 6, "SHA256:3E5D8A..."),
        AppItem("4", "Secure Shell Service", "com.shell.daemon", "4.0", true, 34, 12, "SHA256:1A2B3C..."),
        AppItem("5", "Package Tamper Inspector", "com.audit.tamper", "3.2", false, 32, 4, "SHA256:5E6F7G...")
    ),
    
    val tools: List<ToolItem> = listOf(
        ToolItem("t1", "Dex Decompiler & Inspector", "Extract and inspect classes.dex bytecode and methods.", "Analysis", "Code"),
        ToolItem("t2", "Network Ping & Traceroute", "Test latency and routing paths to target hosts.", "Network", "NetworkCheck"),
        ToolItem("t3", "Port & Service Scanner", "Scan active TCP/UDP ports on localhost or remote IP.", "Security", "Security"),
        ToolItem("t4", "Certificate & SSL Auditor", "Inspect SSL/TLS certificates, trust chains, and pinning.", "Security", "Lock"),
        ToolItem("t5", "ADB Command Injector", "Execute administrative shell commands and automation scripts.", "Terminal", "Terminal"),
        ToolItem("t6", "APK Signer & Verifier", "Validate APK signatures (V1, V2, V3, V4) and RSA blocks.", "Utility", "Verified")
    ),

    val securityChecks: List<SecurityCheck> = listOf(
        SecurityCheck("s1", "SELinux Enforcing Mode", "Kernel security policy state", true, "High"),
        SecurityCheck("s2", "Device Encryption Status", "Storage hardware encryption active", true, "High"),
        SecurityCheck("s3", "USB Debugging Restricted", "ADB interface access authorization", false, "Medium"),
        SecurityCheck("s4", "Developer Options Disabled", "System debug flags configuration", false, "Low"),
        SecurityCheck("s5", "Signature Verification Integrity", "Runtime APK signature check passed", true, "Critical")
    ),

    val logs: List<LogEntry> = listOf(
        LogEntry(1, "22:37:00", "INFO", "LoaderCore", "Brutal Admin Loader v4.7 initialized successfully."),
        LogEntry(2, "22:37:02", "DEBUG", "SysDaemon", "Memory heap allocated: 128MB / 512MB max."),
        LogEntry(3, "22:37:05", "INFO", "NetService", "Active socket listener bound on port 8080."),
        LogEntry(4, "22:37:10", "WARN", "SecurityAudit", "USB Debugging detected active in developer settings."),
        LogEntry(5, "22:37:15", "INFO", "DexInspector", "Loaded 7 dex archives for inspection.")
    ),

    val terminalOutput: String = "Brutal Admin Shell v4.7 [Ready]\nType 'help' for available administrative commands.\n> "
)

class AdminViewModel : ViewModel() {
    private val _state = MutableStateFlow(AdminUiState())
    val state: StateFlow<AdminUiState> = _state.asStateFlow()

    fun runTool(toolId: String, param: String = "") {
        viewModelScope.launch {
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val newLog = when (toolId) {
                "t1" -> LogEntry(System.currentTimeMillis(), time, "INFO", "DexInspector", "Decompiled classes.dex successfully. 4,210 methods indexed.")
                "t2" -> LogEntry(System.currentTimeMillis(), time, "INFO", "NetService", "Ping target 8.8.8.8: avg latency 14.2ms. 0% packet loss.")
                "t3" -> LogEntry(System.currentTimeMillis(), time, "WARN", "PortScan", "Scan completed: ports 80 (HTTP), 443 (HTTPS), 8080 (Proxy) open.")
                "t4" -> LogEntry(System.currentTimeMillis(), time, "INFO", "SSLAudit", "Certificate chain verified. RSA 2048-bit with SHA256.")
                "t5" -> LogEntry(System.currentTimeMillis(), time, "DEBUG", "Shell", "Executed command: '$param'. Exit code 0.")
                else -> LogEntry(System.currentTimeMillis(), time, "INFO", "ToolRunner", "Tool $toolId executed.")
            }
            _state.update { current ->
                current.copy(logs = listOf(newLog) + current.logs)
            }
        }
    }

    fun executeCommand(command: String) {
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val response = when (command.trim().lowercase()) {
            "help" -> "Commands: help, status, clear, list, scan, reboot, exit"
            "status" -> "CPU: ${_state.value.cpuUsage}% | RAM: ${_state.value.ramUsedMb}/${_state.value.ramTotalMb}MB | Root: ${_state.value.isRooted}"
            "list" -> _state.value.apps.joinToString("\n") { "${it.packageName} (${it.versionName})" }
            "scan" -> "Security audit initiated. 5 checks evaluated."
            "clear" -> ""
            else -> "Command not recognized: '$command'. Type 'help'."
        }
        
        _state.update { current ->
            val updatedTerminal = if (command.trim().lowercase() == "clear") {
                "Brutal Admin Shell v4.7 [Ready]\n> "
            } else {
                current.terminalOutput + "$command\n$response\n> "
            }
            current.copy(
                terminalOutput = updatedTerminal,
                logs = listOf(LogEntry(System.currentTimeMillis(), time, "INFO", "Shell", "User entered: $command")) + current.logs
            )
        }
    }

    fun toggleRootStatus() {
        _state.update { it.copy(isRooted = !it.isRooted) }
    }
}
