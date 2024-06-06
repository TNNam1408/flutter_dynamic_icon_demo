package com.example.dynamic_icon_method_flutter_0604

import android.os.Bundle
import android.os.PersistableBundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private var iconChangeManager: IconChangeManager? = null
    private val CHANNEL = "methodChannelKey1"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        iconChangeManager = IconChangeManager(this)
        // Set up the MethodChannel with the same name as defined in Dart
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "getDataFromNativeOriginal" -> {
                    onAliasSelected(
                        "Original Icon"
                    )
                    // Perform platform-specific operations and obtain the result
                    val data = getDataFromNative()

                    // Send the result back to Flutter
                    result.success(data)
                }

                "getDataFromNativeRed" -> {
                    onAliasSelected(
                        "Really Red"
                    )
                    // Perform platform-specific operations and obtain the result
                    val data = getDataFromNative()

                    // Send the result back to Flutter
                    result.success(data)
                }

                "getDataFromNativeGreen" -> {
                    onAliasSelected(
                        "Greatly Green"
                    )
                    // Perform platform-specific operations and obtain the result
                    val data = getDataFromNative()

                    // Send the result back to Flutter
                    result.success(data)
                }

                "getDataFromNativeBlue" -> {
                    onAliasSelected(
                        "Bigly Blue"
                    )
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        iconChangeManager!!.onSaveInstanceState(outState)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        iconChangeManager = IconChangeManager(this)
//
//        val output = findViewById<TextView>(R.id.text_output)
//        val activateButton = findViewById<ToggleButton>(R.id.button_activate)
//
//        output.text = getString(
//            R.string.current_alias,
//            iconChangeManager!!.getCurrentAlias().simpleName
//        )
//
//        activateButton.isChecked = iconChangeManager!!.isIconChangeActivated
//        activateButton.setOnCheckedChangeListener { v: CompoundButton?, checked: Boolean ->
//            switchActivation(
//                checked
//            )
//        }

//        if (iconChangeManager!!.isIconChangeActivated) {
//            val gridView = findViewById<GridView>(R.id.grid_view)
//            gridView.adapter = AliasAdapter(this, iconChangeManager!!)
//            gridView.onItemClickListener =
//                OnItemClickListener { av: AdapterView<*>?, v: View?, p: Int, id: Long ->
//                    onAliasSelected(
//                        p
//                    )
//                }
//        }

//        if (iconChangeManager!!.isIconChangeRefresh(savedInstanceState)) {
//            showIconChangeMessage()
//        }
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        iconChangeManager!!.onSaveInstanceState(outState)
    }

//    private fun switchActivation(enable: Boolean) {
//        findViewById<View>(android.R.id.content).visibility = View.GONE
//        if (enable) {
//            iconChangeManager!!.activateIconChange()
//        } else {
//            if (!iconChangeManager!!.deactivateIconChange()) {
//                Toast.makeText(this, R.string.deactivate_error, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun onAliasSelected(title: String) {
        iconChangeManager!!.setCurrentAlias(iconChangeManager!!.aliases.find { it.title == title }!!)
    }

//    private fun showIconChangeMessage() {
//        val message = if (iconChangeManager!!.isIconChangeActivated) getString(
//            R.string.message_alias_selected,
//            iconChangeManager!!.getCurrentAlias().simpleName
//        ) else getString(R.string.message_alias_reset)
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }

//    private class AliasAdapter(context: Context, manager: IconChangeManager) :
//        ArrayAdapter<Alias?>(context, R.layout.item_alias, manager.aliases) {
//        private val currentAlias = manager.getCurrentAlias()
//        private val drawableDimen =
//            context.resources.getDimensionPixelSize(R.dimen.item_alias_image_size)
//
//        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//        @SuppressLint("UseCompatLoadingForDrawables")
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val view = super.getView(position, convertView, parent) as TextView
//            val drawable = getItem(position)?.let { context.getDrawable(it.iconResId) }
//            drawable!!.setBounds(0, 0, drawableDimen, drawableDimen)
//            view.setCompoundDrawables(null, drawable, null, null)
//            view.alpha = if (isEnabled(position)) 1f else .3f
//            return view
//        }
//
//        override fun isEnabled(position: Int): Boolean {
//            return !getItem(position)!!.equals(currentAlias)
//        }
//    }
}
