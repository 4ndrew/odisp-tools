# $Id: make_messages.awk,v 1.2 2003/10/14 13:29:09 valeks Exp $
BEGIN {
    print "<?xml version=\"1.0\" encoding=\"koi8-r\" ?>\n<messages xmlns:xi=\"http://www.w3.org/2001/XInclude\">";
} 
END {
    print "</messages>";
} 
{
    print "\t<message>"$2"<data><xi:include href=\"messages/"$2".xml\"><xi:fallback/></xi:include></data></message>";
}