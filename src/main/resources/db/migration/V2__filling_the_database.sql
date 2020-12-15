INSERT INTO blogengine.users VALUES
(1, 0, "2020-01-14 14:57:00", "Чернов Игорь", "chernov@mail.ru", "NFnu82vBb877cihuhu42dMck89aapcqoHFfqb", null, null),
(2, 0, "2020-08-19 17:24:00", "Сапогов Ричард", "sapogov@mail.ru", "NFnuJj96fvHtftf2fF7bwvjk89aapcqoHFfqb", null, null),
(3, 0, "2019-10-23 02:33:00", "Жирнов Гога", "goga-jir@mail.ru", "L94nVHhf8rydjjhv772fbGyHbf7a9fbVBusuv3", null, null),
(4, 1, "2018-03-09 08:44:00", "Федосеев Остап", "fedoseev@mail.ru", "Rhbv93vbyfkw322vmscd948gnsbu27cd4444", null, null),
(5, 0, "2020-05-12 13:01:00", "Соловьев Никита", "solov-nik@mail.ru", "vovo983428vds72ygv8ssvg23fgYGUKfv227fs", null, null),
(6, 0, "2020-04-16 10:15:00", "Копыткин Валерий", "kop-valera@mail.ru", "8HbfuUGYVh3355bVljvkHf72bs9djfn2", null, null),
(7, 1, "2020-09-03 12:21:00", "Баранов Илья", "baranov@mail.ru", "H3n9sqofpgnw3gfGGE72gjYGUF7fwqqM9vbsf", null, null);

INSERT INTO blogengine.tags VALUES
(1, "Java"),
(2, "Spring"),
(3, "HTML"),
(4, "Git"),
(5, "MongoDB");

INSERT INTO blogengine.posts VALUES
(1, "0", "NEW", null, 6, "2019-11-04 12:32:00", "Что нового будет в Java 14", "Сопоставление шаблонов позволяет выражать обычную логику «кратко и безопасно». Согласно документации OpenJDK, сейчас существуют только специализированные решения для сопоставления шаблонов, поэтому авторы посчитали, что пришло время существенно расширить использование сопоставления шаблонов в Java.", 15),
(2, "1", "ACCEPTED", 7, 7, "2020-06-04 03:41:00", "Приёмы и хитрости начинающего Java-программиста", "Для конкатенации (сложения) строк в Java используется оператор «+», для примера, в цикле for новый объект может создаваться для каждой новой строки, что приводит к потере памяти и увеличению времени работы программы.", 23),
(3, "1", "DECLINED", 4, 1, "2020-01-23 09:24:00", "Передовой опыт тестирования в Java", "Ещё пару тестов, проверяющих особые случаи или особую бизнес-логику, например что определённые значения в ответе вычислены корректно.", 42),
(4, "0", "NEW", null, 6, "2020-07-11 13:11:00", "Переменные CSS — курс молодого бойца", "Корректное имя переменной может содержать латинские буквы и цифры, подчеркивания, тире. Важно упомянуть, что переменные CSS чувствительны к регистру.", 31),
(5, "1", "ACCEPTED", 4, 1, "2020-02-28 19:22:00", "Spring MVC", "При написании веб-приложений на Java с использованием Spring или без него (MVC/Boot) вы в основном имеете в виду написание приложений, которые возвращают два разных формата данных.", 77),
(6, "0", "DECLINED", 7, 4, "2020-07-19 17:36:00", "Java DevTools: модно не значит хорошо", "Относительно популярности Git — тут работает вот какой момент, мне кажется: разработчики любят что-то очень мощное.", 20),
(7, "1", "DECLINED", 7, 2, "2020-03-08 04:43:00", "Логирование Git revision в Java с помощью Maven", "Если вы посмотрите лог, который Вам вывел maven при сборке, то заметите что он выполняет команду git для получения искомых данных.", 18),
(8, "1", "DECLINED", 4, 2, "2020-01-17 22:15:00", "Знакомство с Spring Data MongoDB", "Чтобы использовать конфигурацию MongoDB, нам нужно реализовать класс AbstractMongoConfiguration.Класс MongoConfig.java будет выглядеть так, как показано ниже. Здесь мы используем аннотации вместо xml.", 37),
(9, "1", "NEW", null, 3, "2020-05-09 00:48:00", "14 вещей, которые я хотел бы знать перед началом работы с MongoDB", "Забыв о порядке сортировки можно сильнее всего разочароваться и потерять больше времени, чем при использовании любой другой неправильной конфигурации. По умолчанию MongoBD использует бинарную сортировку.", 50),
(10, "1", "DECLINED", 4, 1, "2020-04-12 18:58:00", "Руководство по выживанию с MongoDB", "Мы узнали об этом, когда собирались перестроить индекс, а так как нам нужно было снять с индекса уникальность, то процедура проходила в несколько этапов. В MongoDB нельзя построить рядом с индексом такой же, но без уникальности. ", 61),
(11, "1", "ACCEPTED", 7, 4, "2020-02-23 13:37:00", "Внутреннее устройство Git: хранение данных и merge", "Теперь нам нужно выполнить git merge topic, находясь в ветке master. Мы могли бы выбрать коммит E как общего предка, но Git со стратегией recursive делает иначе.", 48),
(12, "0", "ACCEPTED", 7, 3, "2020-01-16 11:44:00", "Семантика в HTML 5", "Вот еще один пример. Все более очевидно, что в HTML не хватает представления машино-читаемого значения понятным для человека, например даты. Это лежит в основе проблемы BBC с микроформатом hCalendar, о ней мы говорили ранее.", 11);

INSERT INTO blogengine.post_comments VALUES
(1, null, 6, 3, "2020-08-25 19:13:00", "Будем помалкивать и продвигать свои идеи без лишнего шумка."),
(2, null, 11, 5, "2020-08-04 16:24:00", "И именно в сторону качественного улучшения механизмов представления движется HTML 5."),
(3, null, 3, 1, "2020-09-13 03:57:00", "Возможно, есть в планах сделать скрипт, которые будет в автоматическом режиме собирать данные о проектах с github'a и других источников."),
(4, 1, 8, 1, "2020-08-28 17:03:00", "Условно говоря, это описывается одной фразой: всё что нужно для жизни уже написано на Java."),
(5, 4, 8, 7, "2020-08-18 05:56:00", "Мне кажется спринг сейчас популярнее JAVA EE, так что да, будут те кто еще будут сидеть на java 8 через 5 лет, но не уверен что это будет так уж массово."),
(6, null, 1, 4, "2020-09-09 09:43:00", "В том то и дело что в рамках поставленной задачи нужен не мгновенный срез."),
(7, null, 2, 6, "2020-08-15 18:36:00", "Статья замечательная и очень подробная.");

INSERT INTO blogengine.tag2post VALUES
(1, 1, 1),
(2, 1, 4),
(3, 2, 1),
(4, 3, 1),
(5, 4, 2),
(6, 4, 3),
(7, 5, 2),
(8, 6, 4),
(9, 7, 1),
(10, 7, 4),
(11, 8, 1),
(12, 8, 5),
(13, 9, 5),
(14, 10, 5),
(15, 11, 1),
(16, 11, 5),
(17, 12, 1),
(18, 12, 3);

INSERT INTO blogengine.post_voters VALUES
(1, 4, 1, "2020-08-08 00:11:00", 1),
(2, 7, 1, "2020-08-08 00:11:00", 1),
(3, 1, 1, "2020-08-08 00:11:00", 1),
(4, 5, 1, "2020-08-08 00:11:00", -1),
(5, 6, 1, "2020-08-08 00:11:00", -1),
(6, 2, 2, "2020-08-08 00:11:00", -1),
(7, 1, 2, "2020-08-08 00:11:00", -1),
(8, 3, 2, "2020-08-08 00:11:00", 1),
(9, 1, 2, "2020-08-08 00:11:00", -1),
(10, 7, 2, "2020-08-08 00:11:00", 1),
(11, 4, 2, "2020-08-08 00:11:00", 1),
(12, 2, 2, "2020-08-08 00:11:00", 1),
(13, 3, 3, "2020-08-08 00:11:00", 1),
(14, 4, 3, "2020-08-08 00:11:00", 1),
(15, 1, 3, "2020-08-08 00:11:00", 1),
(16, 2, 3, "2020-08-08 00:11:00", -1),
(17, 1, 3, "2020-08-08 00:11:00", -1),
(18, 5, 4, "2020-08-08 00:11:00", 1),
(19, 3, 4, "2020-08-08 00:11:00", 1),
(20, 7, 4, "2020-08-08 00:11:00", 1),
(21, 4, 5, "2020-08-08 00:11:00", 1),
(22, 2, 5, "2020-08-08 00:11:00", 1),
(23, 4, 5, "2020-08-08 00:11:00", -1),
(24, 5, 5, "2020-08-08 00:11:00", 1),
(25, 7, 5, "2020-08-08 00:11:00", -1),
(26, 6, 6, "2020-08-08 00:11:00", 1),
(27, 2, 6, "2020-08-08 00:11:00", 1),
(28, 4, 6, "2020-08-08 00:11:00", 1),
(29, 1, 7, "2020-08-08 00:11:00", 1),
(30, 1, 7, "2020-08-08 00:11:00", 1),
(31, 5, 7, "2020-08-08 00:11:00", -1),
(32, 4, 7, "2020-08-08 00:11:00", 1),
(33, 2, 7, "2020-08-08 00:11:00", 1),
(34, 7, 8, "2020-08-08 00:11:00", -1),
(35, 7, 8, "2020-08-08 00:11:00", 1),
(36, 3, 8, "2020-08-08 00:11:00", 1),
(37, 4, 9, "2020-08-08 00:11:00", -1),
(38, 3, 9, "2020-08-08 00:11:00", 1),
(39, 1, 10, "2020-08-08 00:11:00", 1),
(40, 6, 10, "2020-08-08 00:11:00", 1),
(41, 7, 10, "2020-08-08 00:11:00", 1),
(42, 4, 11, "2020-08-08 00:11:00", -1),
(43, 1, 11, "2020-08-08 00:11:00", 1),
(44, 5, 11, "2020-08-08 00:11:00", 1),
(45, 7, 11, "2020-08-08 00:11:00", 1),
(46, 4, 12, "2020-08-08 00:11:00", 1),
(47, 1, 12, "2020-08-08 00:11:00", 1),
(48, 5, 12, "2020-08-08 00:11:00", 1),
(49, 4, 12, "2020-08-08 00:11:00", 1),
(50, 6, 12, "2020-08-08 00:11:00", 1),
(51, 3, 12, "2020-08-08 00:11:00", 1);

INSERT INTO blogengine.global_settings VALUES
(1, "MULTIUSER_MODE", "Многопользовательский режим", "NO"),
(2, "POST_PREMODERATION", "Премодерация постов", "YES"),
(3, "STATISTICS_IS_PUBLIC", "Показывать всем статистику блога", "YES");