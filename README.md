# PeyangBanManager
## 概要
プレイヤーのBANを別サーバーで管理するプラグインです。  
このプラグインを使用するには、[こちら](https://github.com/psac-serve/ban-server)の別サーバーが必要です。
## 元ネタ
このプラグインは[Hypixel Staff Mod Leak](https://www.youtube.com/watch?v=SDTRO0cx0Lc)をもとに作成されており、本家を忠実に再現しております。  
### 注意
当プラグインはこのMODのサポートをしていますが、LynxModを使用しHypixelServerにアクセスした場合、HypixelServerから永久的にアクセスを禁止される可能性がございます。  
また、LynxModを使用してHypixelServerから永久的にアクセスを禁止された場合、当プラグイン[作者](https://github.com/peyang-Celeron/)は一切の責任を負いません。
## インストール方法
+ [こちら](https://github.com/psac-server/ban-serve/releases)から最新ファイルをダウンロードします。
+ ダウンロードしたファイルをpluginsフォルダに配置します。
+ サーバーをリロードまたは再起動します。
+ plugins/PeyangGreatBanServer/config.ymlに、tokenを書き込みます。
### tokenとは
[こちら](https://github.com/psac-serve/ban-server)のサーバーは認証として、Tokenを導入しております。  
Tokenは、１度このサーバーを起動したあと、PeyangBanServer/token.sigファイルに保存されています。
## コマンド
---
### コマンドドキュメントの見方
### /コマンド名
#### 概要
...
#### エイリアス
+ /エイリアス1
+ /エイリアス2
#### 使用方法：/コマンド名 <必須パラメーター> [任意パラメーター]...
#### 権限：pybans.権限
---
### /ban
#### 概要
プレイヤーを**永久的に**アクセス禁止します。モデレーターがアクセス禁止を解除しない限り、プレイヤーはアクセスできません。
### エイリアス
+ /acban
+ /permban
#### 使用方法：/ban <プレイヤーの名前> \[理由\]
#### 権限：pybans.ban
### /tempban
#### 概要
プレイヤーを**一時的に**アクセス禁止します。設定した期間が満了した場合、自動的に解除されます。また、コマンドからの解除も可能です。
#### 使用方法：/tempban <プレイヤーの名前> <期間...> \[理由...\]
##### 期間設定方法
`/tempban hoge 1y 2mo 3d 4h 5h 6h 7m 8s Cheat`と指定した時、hogeというプレイヤーは、`1年2ヶ月3日5時間7分8秒`間、アクセスを禁止されます。  
各パラメータは`1y`や、`1d 1mo`等の順番を入れ替えたり、数が足りなくても動作します。
この場合は`1年`、`１ヶ月と１日`の間アクセスを禁止されます。  
パラメータの名前と指定方法は以下の通りです。

| キーワード | エイリアス | 例  |        説明        |
| :--------: | :--------: | :-: | :----------------: |
|    year    |     y      | 3y  |  年を指定します。  |
|   month    |     mo     | 3mo |  月を指定します。  |
|    day     |     d      | 3d  |  日を指定します。  |
|    hour    |     h      | 3h  | 時間を指定します。 |
|   minute   |   min, m   | 3m  |  分を指定します。  |
|   second   |   sec, s   | 3s  |  秒を指定します。  |
#### 権限：pybans.tempban
### /pardon
#### 概要
アクセス禁止にされたプレイヤーのアクセス禁止を解除します。
#### エイリアス
+ unban
#### 使用法：/pardon <プレイヤーの名前>
#### 権限：pybans.unban
### /bans
#### 概要
プレイヤーのアクセス禁止履歴を表示します。
#### 使用法：/bans \[-a\] <プレイヤーの名前> \[ページ\]
#### 権限：pybans.bans
### /banhelp
#### 概要
このプラグインのヘルプを表示します。
#### 使用法：/banhelp
#### 権限：pybans.help

## 権限
### pybans.member
#### タイプ：**グループ**
#### デフォルト：**有効**
#### 権限概要
サーバーのプレイヤーの初期権限セットです。
#### 内容
+ pybans.notification
### pybans.notification
#### タイプ：**機能権限**
#### 権限概要
プレイヤーがアクセス禁止になった際、チャットにメッセージが表示されます。
---
### pybans.mod
#### タイプ：**グループ**
#### デフォルト：**OP**
#### 権限概要
プレイヤーをアクセス禁止にしたり、アクセス禁止を解除、プレイヤーのアクセス禁止履歴を参照できます。
#### 権限内容
+ pybans.help
+ pybans.ban
+ pybans.tempban
+ pybans.unban
+ pybans.bans
+ *pybans.member*
### pybans.help
#### タイプ：**コマンド権限**
#### デフォルト：*pybans.mod*
#### 権限概要
/banhelp コマンドの使用権限です。
### pybans.ban
#### タイプ：**コマンド権限**
#### デフォルト：*pybans.mod*
#### 権限概要
/ban コマンドの使用権限です。
### pybans.tempban
#### タイプ：**コマンド権限**
#### デフォルト：*pybans.mod*
#### 権限概要
/tempban コマンドの使用権限です。
### pybans.unban
#### タイプ：**コマンド権限**
#### デフォルト：*pybans.mod*
#### 権限概要
/unban コマンドの使用権限です。
### pybans.bans
#### タイプ：**コマンド権限**
#### デフォルト：*pybans.mod*
#### 権限概要
/bans コマンドの使用権限です。

## 謝辞
このプラグインは、以下のAPI/ライブラリを使用しております。
+ [P2P-Develop/PeyangSuperLibrary](https://github.com/P2P-Develop/PeyangSuperLibrary)
+ [FasterXML/jackson](https://github.com/FasterXML/jackson)
+ [Apache CommonsLang3](https://commons.apache.org/proper/commons-lang/)
