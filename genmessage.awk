#
# $Id: genmessage.awk,v 1.13 2004/08/30 10:07:12 valeks Exp $
#
# Утилита для генерации классов сообщений ODISP на основе шаблонов.
# Пример шаблонов:
#
# NAME [пакет] [имя класса] [название ODISP action]
# IMPORT [пакет] (*)
# AUTHOR [автор (для тега @author)] (*)
# DESC [описание сообщения] (*)
# FIELD [имя поля (с заглавной буквы)] [тип поля]
# FCHECK [имя поля] [выражение для проверки в checkMessage (должно возвращать boolean)] (**)
# FDESC [имя поля] [описание поля] (*)
# DEFORIGIN [точка отправления по-умолчанию]
# DEFDEST [точка назначения по-умолчанию]
# DEFID [ReplyId сообщения по-умолчанию]
# DEFROUTABLE [Routable по-умолчанию]
# DEFOOB [OOB по-умолчанию]
# VERBATIM (***)
# Версия для тега @version берется из CVS-тега Id.
#
# (*) Поддерживаются multiline comments, все поля комментариев, должны
# начинаться с нового ключевого слова.
# Например:
# AUTHOR 1 строка
# AUTHOR 2 строка
# (**) Значение по-умолчанию get[имя поля](msg) != null
# (***) VERBATIM включает режим переноса текста в результирующее сообщение. Выключается
#       повторным VERBATIM. Может встречаться несколько раз, но результат будет выведен
#       только в конце сообщения.
#

$1 ~ /^NAME/ {
  package = $2;
  name = $3;
  action = $4;
};

/^\$/ {
  version = $0;
};

$1 ~ /^IMPORT/ {
  if (imports != "") {
    imports = imports "\nimport " $2 ";"
  } else {
    imports = "import " $2 ";";
  }
};
  
$1 ~ /^AUTHOR/ {
  if (author != "") {
    author = author "\n * @author " substr($0, 8);
  } else {
    author = substr($0, 8);
  }
};

$1 ~ /^DESC/ {
  if (desc != "") {
    desc = desc "\n * " substr($0, 6);
  } else {
    desc = substr($0, 6);
  }
};

$1 ~ /^FIELD/ {
  fields_type[$2] = $3;
};

$1 ~ /^FCHECK/ {
  fields_check[$2] = substr($0, 9 + length($2));
};

$1 ~ /^FDESC/ {
  if (fields_desc[$2] != "") {
    fields_desc[$2] = fields_desc[$2] "\n   * " substr($0, 8 + length($2));
  } else {
    fields_desc[$2] = substr($0, 8 + length($2));
  }
};

$1 ~ /^DEFORIGIN/ {
  deforigin = $2;
};

$1 ~ /^DEFDEST/ {
  defdest = $2;
};

$1 ~ /^DEFID/ {
  defid = $2;
};

$1 ~ /^DEFROUTABLE/ {
  defroutable = 1;
}

$1 ~ /^DEFOOB/ {
  printf "WARNING: Generating OOB message " name " \n" > "/dev/fd/2";
  defoob = 1;
}

$1 ~ /^VERBATIM/ {
  if (verbatim == 1) {
    verbatim = 0;
  } else {
    verbatim = 1;
  }
}

{
  if (verbatim != 0) {
    if ($1 != "VERBATIM") {
      verbatimCode = verbatimCode "\n" $0;
    }
  }
}

END {
  gsub(/\\n/, "\n", desc);
  printf  "package " package ";\n\n" \
          "import org.valabs.odisp.common.Message;\n\n";
  
  if (imports != "") {
    printf imports "\n\n";
  }
  
  printf  "/** " desc "\n" \
          " *\n" \
          " * @author " author "\n" \
          " * @author (C) 2004 НПП \"Новел-ИЛ\"\n" \
          " * @version " version "\n" \
          " */\n" \
          "public final class " name " {\n" \
          "  /** Строковое представление сообщения. */\n" \
          "  public static final String NAME = \"" action "\";\n\n";

  for (key in fields_type) {
    printf  "  /** Индекс для поля " key ". */\n" \
            "  private static String idx" toupper(key) " = \"" \
            tolower(key) "\";\n";
  }

  printf  "\n\n" \
          "  /** Запрет на создание объекта. */\n" \
          "  private " name "() { }\n\n";

  printf  "  /** Проверка сообщения на корректность.\n" \
          "   *\n" \
          "   * @param msg Сообщение\n" \
          "   */\n" \
	  "  private static void checkMessage(final Message msg) {\n";
  if (length(fields_type) != 0) {
    print "    try {";
    for (key in fields_type) {
      if (fields_checkstr[key] == "") {
        print "      assert get" key "(msg) != null : \"Message field " key " is null.\";";
      }
    }
    print "    } catch (AssertionError e) {\n" \
          "      System.err.println(\"Message assertion :\" + e.toString());\n"\
          "      e.printStackTrace();\n    }\n";
    print   "    msg.setCorrect(";
    checkstr = "";
    for (key in fields_type) {
      if (fields_check[key] != "") {
        checkstr = fields_check[key];
      } else {
        checkstr = "get" key "(msg) != null"
      }
      if (flag == 1)  {
        printf "      && " checkstr "\n";
      } else {
        printf "      " checkstr "\n"; 
        flag = 1;
      }
    }
  } else {
    print "    msg.setCorrect(\n      true";
  }
  printf  "    );\n" \
          "  }\n\n";

  printf  "  /** Инициализация основных свойств сообщения.\n" \
          "   *\n" \
          "   * @param msg Сообщение.\n";
  if (defdest == "") {
    printf  "   * @param destination Точка назначения.\n";
  }
  if (deforigin == "") {
    printf "   * @param origin Точка отправления.\n";
  }
  printf  "   * @param replyTo Идентификатор сообщения, " \
          "на которое это является ответом.\n" \
	  "   * @return ссылка на инициализированное сообщение\n" \
          "   */\n" \
    "  public static Message setup(final Message msg";
    if (defdest == "") {
      printf  ", final String destination";
    }
    if (deforigin == "") {
      printf ", final String origin";
    }
    if (defreplto == "") {
      printf ", final int replyTo) {\n";
    }
    printf "    msg.setAction(NAME);\n";
    if (defdest == "") {
      printf "    msg.setDestination(destination);\n";
    } else {
      printf "    msg.setDestination(\"" defdest "\");\n";
    }
    if (deforigin == "") {
      printf "    msg.setOrigin(origin);\n";
    } else {
      printf "    msg.setOrigin(\"" deforigin "\");\n";
    }
    if (defreplto == "") {
      printf "    msg.setReplyTo(replyTo);\n";
    } else {
      printf "    msg.setReplyTo(" defreplyto ");\n";
    }
    if (defroutable == 0) {
      printf "    msg.setRoutable(false);\n";
    }
    if (defoob == 1) {
      printf "    msg.setOOB(true);\n";
    }
    printf "    checkMessage(msg);\n"\
      "    return msg;\n" \
      "  }\n\n";

  for (key in fields_type) {
    gsub(/\\n/, "\n", fields_desc[key]);
    printf  "  /** Установить " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg Сообщение над которым производится действо.\n" \
            "   * @param newValue Новое значение для поля.\n" \
	    "   * @return ссылка на сообщение\n" \
            "   */\n" \
            "  public static Message set" key "(final Message msg, final " fields_type[key] " newValue) {\n" \
            "    msg.addField(idx" toupper(key) ", newValue);\n" \
            "    checkMessage(msg);\n" \
            "    return msg;\n" \
            "  }\n\n" \
            "  /** Получить " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg Сообщение над которым производится действо.\n" \
	    "   * @return значение поля\n" \
            "   */\n" \
            "  public static " fields_type[key] " get" key "(final Message msg) {\n" \
            "    return (" fields_type[key] ") msg.getField(idx" toupper(key) ");\n" \
            "  }\n\n";
  }

  printf "  /** Является ли экземпляр сообщением этого типа.\n" \
         "   *\n" \
         "   * @param msg Сообщение.\n" \
         "   * @return true - если является, false - иначе.\n" \
         "   */\n" \
         "  public static boolean equals(final Message msg) {\n" \
         "    return msg.getAction().equals(NAME);\n" \
         "  }\n\n";
  printf "  /** Копирование полей из одного сообщения в другое.\n" \
  		 "  *\n" \
  		 "  * @param dest Получатель.\n" \
  		 "  * @param src Источник.\n" \
  		 "  */\n" \
  		 "  public static void copyFrom(final Message dest, final Message src) {\n";
   for (key in fields_type) {
     printf "    set" key "(dest, get" key"(src));\n";
   }
  printf "  }\n\n";
  printf "  /** Генерирование уникального hash-кода сообщения.\n" \
         "   * Всегда равен 0.\n" \
	 "   * @return hash-код сообщения.\n" \
	 "   */\n" \
	 "  public int hashCode() {\n" \
	 "    return 0;\n" \
	 "  }\n\n";
  printf "  /** Короткий способ заполнения всех полей сообщения сразу.\n" \
    "   * @return ссылку на сообщение\n";
  for (key in fields_desc) {
    printf "   * @param " key " " fields_desc[key] "\n";
  }
  printf "  */\n";
  printf "  public static Message initAll(final Message m";
  for (key in fields_type) {
    printf ",\n                               final " fields_type[key] " " tolower(key);
  }
  printf ") {\n";
  for (key in fields_type) {
    printf "    set" key "(m, " tolower(key) ");\n";
  }
  printf "    return m;\n  }\n\n";

  if (verbatimCode != "") {
    printf verbatimCode;
  }
  printf "\n\n}\n";
 };
