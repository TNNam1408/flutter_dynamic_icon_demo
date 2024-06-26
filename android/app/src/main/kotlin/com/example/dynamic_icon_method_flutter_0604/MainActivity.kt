package com.example.dynamic_icon_method_flutter_0604

import android.os.Bundle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.embedding.android.FlutterFragmentActivity

class MainActivity : FlutterFragmentActivity() {
    private var iconChangeManager: IconChangeManager? = null
    private val CHANNEL = "methodChannelKey1"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        iconChangeManager = IconChangeManager(this)
        // Set up the MethodChannel with the same name as defined in Dart
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger, CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getDataFromNativeOriginal" -> {
                    if (iconChangeManager!!.isIconChangeActivated) {
                        onAliasSelected(
                            "Original Icon"
                        )
                        // Perform platform-specific operations and obtain the result
                        val data = getDataFromNative()

                        // Send the result back to Flutter
                        result.success(data)
                    }

                }

                "getDataFromNativeRed" -> {

                    if (iconChangeManager!!.isIconChangeActivated) {
                        onAliasSelected(
                            "Really Red"
                        )
                        // Perform platform-specific operations and obtain the result
                        val data = getDataFromNative()

                        // Send the result back to Flutter
                        result.success(data)
                    }
                }

                "getDataFromNativeGreen" -> {
                    if (iconChangeManager!!.isIconChangeActivated) {
                        onAliasSelected(
                            "Greatly Green"
                        )
                        // Perform platform-specific operations and obtain the result
                        val data = getDataFromNative()

                        // Send the result back to Flutter
                        result.success(data)
                    }
                }

                "getDataFromNativeBlue" -> {

                    if (iconChangeManager!!.isIconChangeActivated) {

                        onAliasSelected(
                            "Bigly Blue"
                        )
                        // Perform platform-specific operations and obtain the result
                        val data = getDataFromNative()

                        // Send the result back to Flutter
                        result.success(data)
                    }

                }

                "enableDynamicIcon" -> {
                    iconChangeManager!!.activateIconChange()
                    // Perform platform-specific operations and obtain the result
                    val data = getDataFromNative()

                    // Send the result back to Flutter
                    result.success(data)
                }


                "disableDynamicIcon" -> {
                    iconChangeManager!!.deactivateIconChange()
                    // Perform platform-specific operations and obtain the result
                    val data = getDataFromNative()

                    // Send the result back to Flutter
                    result.success(data)
                }

                else -> {
                    result.notImplemented()
                }
            }
        }
    }

    private fun getDataFromNative(): String {
        // Perform platform-specific operations to fetch the data
        return "Data from Native"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        iconChangeManager!!.onSaveInstanceState(outState)
    }

    private fun onAliasSelected(title: String) {
        iconChangeManager!!.setCurrentAlias(iconChangeManager!!.aliases.find { it.title == title }!!)
    }
}
