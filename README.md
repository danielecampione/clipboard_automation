# Clipboard Automation - Clipboard History Automation on Microsoft Windows 11 and 10

## Description
This Java 1.8 project with JavaFX automates the use of multiple clipboard items available in Microsoft Windows 11 and 10. The program:

1. Automatically opens Notepad
2. Uses Win+V to trigger the "Clipboard History" feature of Windows 11 or 10
3. Pastes clipboard items in reverse order, for the number of items specified by the user
4. Optionally adds line breaks at the end of each pasted item

## Screenshot
![Png](https://i.ibb.co/3mtXcdNJ/Immagine-2025-07-05-143113.png)

## Prerequisites
- Microsoft Windows 11 or 10 with Clipboard History feature already enabled
- Java 1.8 with JavaFX
- Clipboard items already copied in the system clipboard, matching the number of items specified by the user in the application

## Project Structure
- `Main.java` - Main class to start the application
- `ClipboardGUI.java` - JavaFX graphical interface
- `ClipboardAutomation.java` - Business logic for automation

## How to Use
1. Make sure the desired number of clipboard items are copied before starting the application
2. Run the program
3. Click on "Start Automation" in the graphical interface
4. The program will launch Notepad and automatically paste the items

## Technical Notes
- Uses Java’s Robot class for key automation
- Introduces appropriate delays to ensure smooth rendering
- Clean separation between business logic and GUI
- Compatible with Java 1.8

## Timing and Delays
- Waits a few seconds after pressing Win+V to allow the Clipboard History UI to render
- Includes short pauses between each paste operation

---

# Clipboard Automation - Automazione appunti della "Cronologia Appunti" di Microsoft Windows 11 e 10

## Descrizione
Questo progetto Java 1.8 con JavaFX automatizza l'utilizzo degli appunti multipli di Microsoft Windows 11 e 10. Il programma:

1. Apre automaticamente Notepad
2. Utilizza Win+V per aprire la "Cronologia Appunti", ovvero gli appunti multipli, di Windows 11 o 10
3. Incolla gli elementi degli appunti nell'ordine inverso, per il numero di elementi definito dall'utente
4. Aggiunge automaticamente interruzioni di riga al termine, se richiesto dall'utente

## Schermata catturata
![Png](https://i.ibb.co/3mtXcdNJ/Immagine-2025-07-05-143113.png)

## Prerequisiti
- Sistema operativo Microsoft Windows 11 o 10 con la funzionalità "Cronologia Appunti" già attiva
- Java 1.8 con JavaFX
- Elementi già copiati negli appunti di sistema prima dell'esecuzione del processo da parte del programma, nella stessa quantità indicata dall'utente nell'applicativo

## Struttura del Progetto
- `Main.java` - Classe principale per l'avvio dell'applicazione
- `ClipboardGUI.java` - Interfaccia grafica JavaFX
- `ClipboardAutomation.java` - Logica di business per l'automazione

## Come Utilizzare
1. Assicurarsi di aver copiato gli elementi negli appunti di Windows 11 o 10, nella stessa quantità indicata dall'utente nell'applicativo
2. Eseguire il programma
3. Cliccare su "Avvia Automazione" nell'interfaccia grafica
4. Il programma aprirà Notepad e incollerà automaticamente gli elementi

## Note Tecniche
- Utilizza la classe Robot di Java per l'automazione dei tasti
- Implementa pause appropriate per evitare problemi di rendering
- Separazione netta tra logica di business e interfaccia grafica
- Compatibile con Java 1.8

## Timing e Pause
- alcuni secondi di attesa dopo Win+V per il rendering della "Cronologia Appunti" e tra ogni operazione di incolla

---

# Clipboard Automation - 自动化使用 Microsoft Windows 11 和 10 的“剪贴板历史记录”

## 描述
本项目基于 Java 1.8 和 JavaFX，旨在自动化使用 Microsoft Windows 11 和 10 中的多重剪贴板功能。程序功能包括：

1. 自动打开记事本（Notepad）
2. 使用 Win+V 打开 Windows 剪贴板历史记录界面
3. 将剪贴板中的内容按照用户设定的数量，倒序粘贴
4. 可选功能：在每个粘贴项后添加换行符

## 屏幕截图
![Png](https://i.ibb.co/3mtXcdNJ/Immagine-2025-07-05-143113.png)

## 先决条件
- 启用了“剪贴板历史记录”功能的 Microsoft Windows 11 或 10
- 安装 Java 1.8 并包含 JavaFX
- 在运行程序前，已将相应数量的内容复制到系统剪贴板

## 项目结构
- `Main.java` - 应用程序的主类
- `ClipboardGUI.java` - JavaFX 图形用户界面
- `ClipboardAutomation.java` - 执行自动化逻辑的类

## 使用方法
1. 在启动程序之前，确保已复制所需数量的内容到剪贴板
2. 运行程序
3. 在图形界面中点击“启动自动化”按钮
4. 程序将打开记事本并自动粘贴内容

## 技术说明
- 使用 Java 的 Robot 类实现键盘事件自动化
- 插入合理的延时以确保操作稳定执行
- 业务逻辑与图形界面代码分离
- 兼容 Java 1.8

## 延时说明
- 在按下 Win+V 后等待几秒，以确保剪贴板历史界面渲染完成
- 每次粘贴操作之间也插入了短暂延时
