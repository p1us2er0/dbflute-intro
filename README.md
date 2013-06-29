dbflute-intro

***dbflute-introとは*********************************************
dbfluteの導入(セットアップ)ツールです。

すでにある導入ツール、方法
http://dbflute.seasar.org/ja/environment/setup/index.html
 ・eclipse-plugin「EMecha」
 ・maven-plugin「DBFlute Maven Plugin」
 ・テンプレートを利用して手動

dbflute-introのコンセプト
 java開発者以外にも使いやすいように。
 →javaが実行できる環境は必要ですが、eclipseやmavenの知識は不要です。

 DBFluteのO/Rマッパー以外の機能を手軽に利用できるように。
 O/Rマッパーとして使わなくても(使えなくても)便利な機能がたくさんあります。
  SchemaHTML ○
  HistoryHTML ○
  SchemaSyncCheck ○
  CraftDiff △
  LoadDataReverse
  PropertiesHTML
  ReplaceSchema
  AlterCheck
  FreeGen
  ※◯がついている機能がdbflute-introで簡単に利用できます。

***デモ**********************************************************
1. ダウンロード
 1-1. dbflute-intro
  https://bitbucket.org/p1us3inus2er0/dbflute-intro/downloads/dbflute-intro-0.0.1-SNAPSHOT-jar-with-dependencies.jar

 1-2. デモに使用するDB
  https://www.seasar.org/svn/dbflute/trunk/dbflute-basic-example/src/main/resources/exampledb/exampledb.h2.db
  ※参考 ER図 http://dbflute.seasar.org/ja/view/exampledb/index.html?goto=1

 1-3. フォルダ配置
  demoフォルダを作って、1-1、1-2のファイルを配置。

  dbflute-intro-demo
    ├dbflute-intro-0.0.1-SNAPSHOT-jar-with-dependencies.jar
    └db
         ├exampledb.h2.db
         ├exampledb_st.h2.db ・・・  1-2のファイルをコピー
         └exampledb_it.h2.db  ・・・  1-2のファイルをコピー

2. dbflute-introのセットアップ
 2-1. dbflute-introの実行
  java -jar dbflute-intro-0.0.1-SNAPSHOT-jar-with-dependencies.jar
  ※macの場合はjarダブルクリックでもOK。
   (新しいOSでセキュリティ警告が出るため、初回は右クリックから選択して実行。)

 2-2. dbfluteクライアントのダウンロード
  バージョンを入力して、「OK」を押下。
  ※デフォルトは最新バージョンになっています。

 2.3. 初期設定(管理したいDBごとに1回)
  Client Project = exampledb (任意の名前)
  Database = H2 (RDBの種類)
  Url =  jdbc:h2:file:../../db/exampledb (DB接続URL。RDBごとに記述が異なります。)
  Schema = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
  User = sa (DBの接続ユーザ名)
  Password = 空 (パスワード)
  Jdbc Driver Jar Path = 空 (H2の場合はDBFluteクライアントにバンドルされているため不要)
  DBFlute Version = 1.0.4.B(ダンロードしているDBFluteクライアントの任意のバージョン)

  SchemaSyncCheckの+を押下。
  env = it
  Url = jdbc:h2:file:../../db/exampledb_it (DB接続URL。RDBごとに記述が異なります。)
  Schema = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
  User = sa (DBの接続ユーザ名)
  Password = 空 (パスワード)

  SchemaSyncCheckの+を押下。
  env = st
  Url = jdbc:h2:file:../../db/exampledb_st (DB接続URL。RDBごとに記述が異なります。)
  Schema = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
  User = sa (DBの接続ユーザ名)
  Password = 空 (パスワード)
 「Client creation」を押下。

3. 情報取得、参照
　3-1. DB情報取得、参照。
  「jdbc,doc」を押下。
  「ScheamHTML」を押下して、htmlを確認。
  「HistoryHTML」を押下して、htmlを確認。※現時点では差分がないため、参照できません。

 3-2. 別環境のDBとDDLレベルの差分確認。
  「schemaSyncCheck」を押下して、「it」を選択。
 　「SyncCheckHTML」を押下して、 「it」を選択して、htmlを確認。※現時点では差分がないため、参照できません。

4. DB情報変更後にDB情報取得、参照。
 4-1. DBに接続する。
  exampledb.h2.dbに任意の方法で接続。

 4-2. DDL発行。
  ALTER TABLE PUBLIC.MEMBER ADD TEST_COLUMN VARCHAR(10);
  CREATE TABLE TEST_TABLE(TEST_ID INTEGER IDENTITY NOT NULL PRIMARY KEY);

 4-３. DB情報取得、参照。
  「jdbc,doc」を押下。
  「ScheamHTML」を押下して、htmlを確認。
  「HistoryHTML」を押下して、htmlを確認。

 4-4. 別環境のDBとDDLレベルの差分確認。
  「schemaSyncCheck」を押下して、「it」を選択。
 　「SyncCheckHTML」を押下して、 「it」を選択して、htmlを確認。

 4-5. DDLを発行して、差分をなくす。
  exampledb_it.h2.dbに任意の方法で接続して、4-2のDDLを発行。

 4-6. 別環境のDBとDDLレベルの差分確認。
  「schemaSyncCheck」を押下して、「it」を選択。
 　「SyncCheckHTML」を押下して、 「it」を選択して、htmlを確認。※現時点では差分がないため、参照できません。

5. 別環境のDBとDMLレベルの差分確認。
 5-1. 前提知識
  http://dbflute.seasar.org/ja/manual/function/genbafit/projectfit/craftdiff/

 5-2. DML差分のための準備
  以下のフォルダに、「craftdiff」フォルダを作成。
   demo/dbflute-intro/dbflute_exampledb/schema

  「craftdiff」フォルダに「craft-schema.sql」を作成。
    -- #df:assertEquals(MemberStatus)#
    select MEMBER_STATUS_CODE as KEY, MEMBER_STATUS_NAME, DISPLAY_ORDER
      from MEMBER_STATUS
     order by KEY
    ;

 5-3. DB情報取得、参照。
 「jdbc,doc」を押下。
 「HistoryHTML」を押下して、htmlを確認。

 5-4. 別環境のDBとDMLレベルの差分確認。
  「schemaSyncCheck」を押下して、「it」を選択。
 　「SyncCheckHTML」を押下して、 「it」を選択して、htmlを確認。

 TODO

end.
