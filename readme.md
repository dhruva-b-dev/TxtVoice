# TxtVoice

TxtVoice is an Android accessibility and communication application built with Kotlin and Jetpack Compose.

The app provides two primary features:

- 🎤 Speech-to-Text (Live Transcription)
- 🔊 Text-to-Speech (Quick Speak)

It is designed to help users communicate quickly through voice transcription and spoken text generation.

---

## 📱 Screenshots

| Live Transcribe | Quick Speak |
|----------------|-------------|
| Speech-to-Text | Text-to-Speech |
| Add Screenshot | Add Screenshot |

---

## ✨ Features

### 🎤 Live Transcribe

Convert spoken words into text in real time.

Features:

- Real-time speech recognition
- Continuous listening mode
- High transcription accuracy
- Pause transcription
- Clear transcript instantly
- Simple and distraction-free UI

### 🔊 Quick Speak

Convert text into spoken audio.

Features:

- Type any message and speak instantly
- Predefined quick phrases
- Fast communication support
- Clean and accessible interface
- Uses Android TextToSpeech API

Quick phrases include:

- Hello
- Thank You
- Bathroom?
- Need Help
- Yes
- No

---

## 🏗️ Built With

### Language

- Kotlin

### UI

- Jetpack Compose
- Material 3

### Architecture

- MVVM
- State Management with ViewModel

### Android APIs

- SpeechRecognizer
- TextToSpeech

### Dependency Injection

- Hilt

---

## 📂 Project Structure

```
com.dhruva.txtvoice
│
├── ui
├── screens
├── components
├── viewmodel
├── speech
├── tts
└── utils
```

---

## 🚀 Getting Started

### Requirements

- Android Studio Narwhal or newer
- Android SDK 24+
- Kotlin 2.x

### Clone Repository

```bash
git clone https://github.com/dhruva-b-dev/TxtVoice.git
```

### Open Project

1. Open Android Studio
2. Select "Open"
3. Choose the project folder
4. Sync Gradle
5. Run on device/emulator

---

## 🔐 Permissions

The application requires:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
```

---

## 🎯 Use Cases

- Accessibility assistance
- Communication support
- Speech practice
- Hands-free interaction
- Quick phrase communication

---

## 📸 Future Enhancements

- Multi-language support
- Save transcription history
- Export transcripts
- Voice customization
- Offline speech recognition
- Dark/Light theme support

---

## 👨‍💻 Author

Dhruva Bhatt

Senior Android Developer | Kotlin | Jetpack Compose | MVVM

GitHub:
https://github.com/dhruva-b-dev

---