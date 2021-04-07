# Установка IBM InfoSphere Optim на одном узле

Эта статья описывает порядок действий по установке полнофункциональной
системы IBM Optim на одном физическом или виртуальном сервере.

Такая система совмещает на одном сервере функции рабочей станции
разработчика или администратора, сервера Optim, сервера репозитория
Optim, а также компоненты Repository Tier, Client Tier, Engine Tier и
Services Tier из состава поставляемых с Optim инструментов управления
метаданными (Information Governance Catalog - IGC, Information
Analyzer - IA). Инструменты управления метаданными являются частью
платформы IBM Information Server.

Данный вариант установки не рекомендован к практическому применению,
поскольку все компоненты конкурируют за ресурсы единственной
вычислительной системы, а также в силу ресурсных ограничений на
поставляемую версию инструментов управления метаданными (разрешено
использование не более 6 процессорных ядер).

Инструкция готовилась на основе следующих версий программных
компонентов:
* Microsoft Windows Server 2012;
* Optim 11.3 Fix Pack 8;
* Db2 11.1 Fix Pack 6;
* Information Server 11.7;
* Google Chrome 89.0.4389.114.


# Официальные материалы по установке

Рекомендуется дополнительно ознакомиться с официальными руководствами
по планированию и установке:
* [инструкции по обновлению и первичной установке Optim](https://www.ibm.com/support/pages/detailed-instructions-update-or-install-ibm-infosphere-optim-1130-latest-fix-pack);
* [примечания к выпускам Optim](https://www.ibm.com/support/knowledgecenter/SSMLNW_11.3.0/com.ibm.nex.designer.doc/topics/Release_Notes.html);
* [описание сценариев установки Optim](https://www.ibm.com/support/knowledgecenter/SSMLNW_11.3.0/com.ibm.nex.install.doc/topics/opinstall_installation_scenarios.html);

----

# Системные требования

Для установки требуется физический или виртуальный сервер,
соответствующий следующим требованиям:
* операционная система Windows Server 2016 либо Windows Server 2012,
  с установленными актуальными патчами;
* 4-6 процессорных ядер, частотой не ниже 2 ГГц (оптимально
  использование современных процессоров с частотой от 3 ГГц и выше);
* не менее 24 Гбайт оперативной памяти (рекомендуется 32 Гбайт);
* не менее 500 Гбайт свободного дискового пространства для размещения
  приложений и данных (может потребоваться больший объём, в
  зависимости от размера источников данных и характера настраиваемых
  процессов маскирования);
* учётная запись для выполнения установки, соответствующая следующим
  требованиям:
   * логин учётной записи может состоять из латинских букв и цифр,
     использование кириллицы или специальных символов не допусается;
   * наличие прав локального администратора на сервере, где выполняется
     установка.

Рекомендуется предварительно установить следующие программы:
* Notepad++;
* 7zip;
* браузер Mozilla Firefox или Google Chrome актуальной версии.

Рекомендуется использование "чистого" образа операционной системы, на
котором ранее не проводилась установка компонентов решения и другого
программного обеспечения.

При наличии информации о контрольных суммах файлов дистрибутивов,
их рекомендуется проверить на целостность. На платформе Windows
для этого можно использовать встроенную утилиту `certUtil`,
вызвав её в командной строке в следующем формате:
`certUtil -hashfile ИмяФайла ТипХеша`.<br/>
Поддерживаемые типы хеш-кодов включают:
MD2, MD4, MD5, SHA1, SHA256, SHA384, SHA512.

----

# Предлагаемый порядок установки

Для экономии вычислительных ресурсов в данном сценарии предлагается
использование единого экземпляра Db2 для ведения репозиториев Optim и
Information Server. Такая конфигурация не рекомендована для
практического использования, так как может осложнить процедуры
применения пакетов обновлений Db2, Optim и Information Server.

Предлагается следующий порядок установки компонентов системы:
1. [Установка и первичная настройка Db2](OptimDb2Install)
2. [Установка Optim](OptimOptimInstall)
3. Первоначальная настройка Optim
     * [Настройка ядра Optim](OptimInitialConfig)
     * [Настройка Web-приложений Optim](OptimWebConfig)
     * [Настройка Optim Connection Manager и Optim Designer](OptimDesignerConfig)
4. Установка Information Server
     * [Установка Information Server из дистрибутива](OptimIisInstall)
     * [Применение обновлений Information Server](OptimIisUpdate)
     * [Первоначальная настройка Information Server](OptimIisConfig)
5. Базовая настройка Information Server.
6. Подключение источников данных к Optim.
7. Подключение источников данных к Information Server.
8. Проверка импорта метаданных и профилирования.
9. Проверка извлечения и загрузки данных.