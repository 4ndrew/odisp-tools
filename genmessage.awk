#
# $Id: genmessage.awk,v 1.2 2004/06/21 13:09:43 dron Exp $
#
# ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
# ������ ��������:
#
# NAME [�����] [��� ������] [�������� ODISP action]
# AUTHOR [����� (��� ���� @author)]
# DESC [�������� ���������]
# FIELD [��� ���� (� ��������� �����)] [��� ����]
# FCHECK [��� ����] [��������� ��� �������� � checkMessage (������ ���������� boolean)]
# FDESC [��� ����] [�������� ����]
# ������ ��� ���� @version ������� �� CVS-���� Id.
#

$1 ~ /^NAME/ {
  package = $2;
  name = $3;
  action = $4;
  };

/^\$/ {
  version = $0;
  };
  
$1 ~ /^AUTHOR/ {
  author = substr($0, 7);
  };

$1 ~ /^DESC/ {
  desc = substr($0, 6);
  };

$1 ~ /^FIELD/ {
  fields_type[$2] = $3;
  };

$1 ~ /^FCHECK/ {
  fields_check[$2] = substr($0, 9 + length($2));
  };

$1 ~ /^FDESC/ {
  fields_desc[$2] = substr($0, 7 + length($2));
  };

END {
  printf  "package " package ";\n\n" \
          "import com.novel.odisp.common.Message;\n\n" \
          "/** " desc "\n" \
          " *\n" \
          " * @author " author "\n" \
          " * @author (C) 2004 ��� \"�����-��\"\n" \
          " * @version " version " \n" \
          " */\n" \
          "public class " name " {\n" \
          "  /** ��������� ������������� ��������� */\n" \
          "  public static final String NAME = \"" action "\";\n\n";

  for (key in fields_type) {
    printf  "  /** ������ ��� ���� " key "*/\n" \
            "  public static final String " toupper(key) "_IDX = \"" \
            tolower(key) "\";\n";
  }

  printf  "\n\n" \
          "  /** ������ �� �������� ������� */\n" \
          "  private " name "() {}\n\n";

  printf  "  /** �������� ��������� �� ������������.\n" \
          "   *\n" \
          "   * @param msg ���������\n" \
          "   */\n" \
          "  private static void checkMessage(final Message msg) {\n" \
          "    msg.setCorrect(\n";

  flag = 0;
  for (key in fields_check) {
    if (flag == 1)  {
      printf "      && " fields_check[key] "\n";
    } else {
      printf "      " fields_check[key] "\n"; 
      flag = 1;
    }
  }
  if (flag == 0) {
    printf "true\n";
  }
  printf  "    );\n" \
          "  }\n\n";

  printf  "  /** ������������� �������� ������� ���������.\n" \
          "   *\n" \
          "   * @param msg ���������.\n" \
          "   * @param destination ����� ����������.\n" \
          "   * @param origin ����� �����������.\n" \
          "   * @param replyTo ������������� ���������, " \
          "�� ������� ��� �������� �������.\n" \
          "   */\n" \
          "  public static void setup(Message msg, final String destination,\n" \
          "                            final String origin, final int replyTo) {\n" \
          "    msg.setAction(NAME);\n"\
          "    msg.setDestination(destination);\n"\
          "    msg.setOrigin(origin);\n"\
          "    msg.setReplyTo(replyTo);\n"\
          "    msg.setRoutable(false);\n"\
          "    checkMessage(msg);\n"\
          "  }\n\n";

  for (key in fields_type) {
    printf  "  /** ���������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   * \n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
            "   * @param newValue ����� �������� ��� ����.\n" \
            "   */\n" \
            "  public static final void set" key "(Message msg, " fields_type[key] " newValue) {\n" \
            "    msg.addField(" toupper(key) "_IDX, newValue);\n" \
            "    checkMessage(msg);\n" \
            "  }\n\n" \
            "  /** �������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   * \n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
            "   * @param newValue ����� �������� ��� ����.\n" \
            "   */\n" \
            "  public static final " fields_type[key] " get" key "(Message msg) {\n" \
            "    return (" fields_type[key] ") msg.getField(" toupper(key) "_IDX);\n" \
            "  }\n\n";
  }

  printf "  /** �������� �� ��������� ���������� ����� ����.\n" \
         "   *\n" \
         "   * @param msg ���������.\n" \
         "   * @return true - ���� ��������, false - �����.\n" \
         "   */\n" \
         "  public static final boolean equals(final Message msg) {\n" \
         "    return msg.getAction().equals(NAME);\n" \
         "  }\n\n";
  printf "}\n";
  };
