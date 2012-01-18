package tyson;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length == 0) {
            System.out.println("Usage: tyson <remote host>");
            return;
        }

        String remoteHost = args[0];
        new Tyson().punchHolesFor(remoteHost);
    }

}
