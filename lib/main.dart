import 'dart:math';

import 'package:dynamic_icon_method_flutter_0604/services.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  void fetchDataFromNative(String counter) async {
    try {
      final String result =
          await platformChannel.invokeMethod('getDataFromNative$counter');
      print('Result from Native: $result');
    } on PlatformException catch (e) {
      print('Error: ${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('Flutter Demo Method Channel'),
      ),
      body: Center(
        child: GridView.count(
          crossAxisCount: 2,
          children: [
            SampleButton(
              text: "Sample Button Original",
              onPressed: () {
                fetchDataFromNative("Original");
              },
            ),
            SampleButton(
              text: "Sample Button Red",
              onPressed: () {
                fetchDataFromNative("Red");
              },
            ),
            SampleButton(
              text: "Sample Button Green",
              onPressed: () {
                fetchDataFromNative("Green");
              },
            ),
            SampleButton(
              text: "Sample Button Blue",
              onPressed: () {
                fetchDataFromNative("Blue");
              },
            ),
          ],
        ),
      ),
    );
  }
}

class SampleButton extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;

  const SampleButton({
    Key? key,
    required this.text,
    this.onPressed,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onPressed,
      child: Container(
        padding: EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: Colors.blue,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Text(
          text,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
