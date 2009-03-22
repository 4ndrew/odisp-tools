# ODISP -- Message Oriented Middleware
# Copyright (C) 2003-2005 Valentin A. Alekseev
# Copyright (C) 2003-2005 Andrew A. Porohin 
# 
# ODISP is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, version 2.1 of the License.
# 
# ODISP is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public License
# along with ODISP.  If not, see <http://www.gnu.org/licenses/>.
#
#
# $Id: genmessage.awk,v 1.18 2005/03/30 13:52:33 dron Exp $
#
# DEPRECATED.
#
# ������� ��� ��������� ������� ��������� ODISP �� ������ ��������.
# ������ ��������:
#
# NAME [�����] [��� ������] [�������� ODISP action]
# IMPORT [�����] (*)
# AUTHOR [����� (��� ���� @author)] (*)
# DESC [�������� ���������] (*)
# FIELD [��� ���� (� ��������� �����)] [��� ����]
# FCHECK [��� ����] [��������� ��� �������� � checkMessage (������ ���������� boolean)] (**)
# FDESC [��� ����] [�������� ����] (*)
# DEFORIGIN [����� ����������� ��-���������]
# DEFDEST [����� ���������� ��-���������]
# DEFID [ReplyId ��������� ��-���������]
# DEFROUTABLE [Routable ��-���������]
# DEFOOB [OOB ��-���������]
# VERBATIM (***)
# ������ ��� ���� @version ������� �� CVS-���� Id.
#
# (*) �������������� multiline comments, ��� ���� ������������, ������
# ���������� � ������ ��������� �����.
# ��������:
# AUTHOR 1 ������
# AUTHOR 2 ������
# (**) �������� ��-��������� get[��� ����](msg) != null
# (***) VERBATIM �������� ����� �������� ������ � �������������� ���������. �����������
#       ��������� VERBATIM. ����� ����������� ��������� ���, �� ��������� ����� �������
#       ������ � ����� ���������.
#

$1 ~ /^NAME/ {
  package = $2;
  name = $3;
  action = $4;
  paramc = 0;
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
  fields_order[paramc] = $2;
  paramc++;
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
          " * @version " version "\n" \
          " */\n" \
          "public final class " name " {\n" \
          "  /** ��������� ������������� ���������. */\n" \
          "  public static final String NAME = \"" action "\";\n\n";

  for (key in fields_type) {
    printf  "  /** ������ ��� ���� " key ". */\n" \
            "  private static String idx" toupper(key) " = \"" \
            tolower(key) "\";\n";
  }

  printf  "\n\n" \
          "  /** ������ �� �������� �������. */\n" \
          "  private " name "() { }\n\n";

  printf  "  /** �������� ��������� �� ������������.\n" \
          "   *\n" \
          "   * @param msg ���������\n" \
          "   */\n" \
	  "  private static void checkMessage(final Message msg) {\n";

  # old awk interpreter not support arrays as length() argument
  arrlen = 0;
  for (key in fields_type) {
    arrlen++;
  }
  
  if (arrlen != 0) {
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

  printf  "  /** ������������� �������� ������� ���������.\n" \
          "   *\n" \
          "   * @param msg ���������.\n";
  if (defdest == "") {
    printf  "   * @param destination ����� ����������.\n";
  }
  if (deforigin == "") {
    printf "   * @param origin ����� �����������.\n";
  }
  printf  "   * @param replyTo ������������� ���������, " \
          "�� ������� ��� �������� �������.\n" \
	  "   * @return ������ �� ������������������ ���������\n" \
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
    printf  "  /** ���������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
            "   * @param newValue ����� �������� ��� ����.\n" \
	    "   * @return ������ �� ���������\n" \
            "   */\n" \
            "  public static Message set" key "(final Message msg, final " fields_type[key] " newValue) {\n" \
            "    msg.addField(idx" toupper(key) ", newValue);\n" \
            "    checkMessage(msg);\n" \
            "    return msg;\n" \
            "  }\n\n" \
            "  /** �������� " key ".\n" \
            "   * " fields_desc[key] "\n" \
            "   *\n" \
            "   * @param msg ��������� ��� ������� ������������ �������.\n" \
	    "   * @return �������� ����\n" \
            "   */\n" \
            "  public static " fields_type[key] " get" key "(final Message msg) {\n" \
            "    return (" fields_type[key] ") msg.getField(idx" toupper(key) ");\n" \
            "  }\n\n";
  }

  printf "  /** �������� �� ��������� ���������� ����� ����.\n" \
         "   *\n" \
         "   * @param msg ���������.\n" \
         "   * @return true - ���� ��������, false - �����.\n" \
         "   */\n" \
         "  public static boolean equals(final Message msg) {\n" \
         "    return msg.getAction().equals(NAME);\n" \
         "  }\n\n";
  printf "  /** ����������� ����� �� ������ ��������� � ������.\n" \
  		 "  *\n" \
  		 "  * @param dest ����������.\n" \
  		 "  * @param src ��������.\n" \
  		 "  */\n" \
  		 "  public static void copyFrom(final Message dest, final Message src) {\n";
   for (key in fields_type) {
     printf "    set" key "(dest, get" key"(src));\n";
   }
  printf "  }\n\n";
  printf "  /** ������������� ����������� hash-���� ���������.\n" \
         "   * ������ ����� 0.\n" \
	 "   * @return hash-��� ���������.\n" \
	 "   */\n" \
	 "  public int hashCode() {\n" \
	 "    return 0;\n" \
	 "  }\n\n";
  printf "  /** �������� ������ ���������� ���� ����� ��������� �����.\n" \
    "   * @return ������ �� ���������\n";
  for (key in fields_desc) {
    printf "   * @param " tolower(key) " " fields_desc[key] "\n";
  }
  printf "  */\n";
  printf "  public static Message initAll(final Message m";
  
  for (i = 0; i < paramc; i++) {
    key = fields_order[i];
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
