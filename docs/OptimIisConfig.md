# Настройка IBM Information Server для работы с IBM Optim

Предыдущий этап:
[установка обновлений Information Server](OptimIisUpdate).

Выполняется:
1. Отключение автоматического запуска DataStage Flow Designer
1. Первичная настройка Information Analyzer
1. Настройка рекомендуемых параметров Information Analyzer

Дополнительные файлы дистрибутивов не требуются.

----

## 1. Отключение автоматического запуска DataStage Flow Designer



HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Run
IBM_DataStage_Flow_Designer
C:\IBM\InformationServer\ASBNode\CognitiveDesignerEngine\startCognitiveDesignerServer.bat
