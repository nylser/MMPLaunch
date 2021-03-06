package de.mineguild.utils;

import java.util.*;

public class BasicArgsParser {

    private Set<String> flagArgs = new HashSet<String>();
    private Set<String> valueArgs = new HashSet<String>();
    ;

    public void addFlagArg(String arg) {
        flagArgs.add(arg);
    }

    public void addValueArg(String arg) {
        valueArgs.add(arg);
    }

    public ArgsContext parse(String[] args) {
        List<String> leftOver = new ArrayList<String>();
        Set<String> flags = new HashSet<String>();
        Map<String, String> values = new HashMap<String, String>();

        boolean processingFlags = true;
        String wantingFlag = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                if (!processingFlags) {
                    throw new IllegalArgumentException("Flags must come first");
                }
                if (arg.length() == 1) {
                    throw new IllegalArgumentException("Flag with no name");
                }
                String flag = arg.substring(1);
                if (valueArgs.contains(flag)) {
                    wantingFlag = flag;
                } else if (flagArgs.contains(flag)) {
                    flags.add(flag);
                } else {
                    throw new IllegalArgumentException("Unknown flag " + flag);
                }
            } else if (wantingFlag != null) {
                values.put(wantingFlag, arg);
                wantingFlag = null;
            } else {
                processingFlags = false;
                leftOver.add(arg);
            }
        }

        return new ArgsContext(flags, values, leftOver);
    }

    public static class ArgsContext {

        private List<String> args;
        private Set<String> flags;
        private Map<String, String> values;

        private ArgsContext(Set<String> flags, Map<String, String> values, List<String> args) {
            this.args = args;
            this.flags = flags;
            this.values = values;
        }

        public List<String> getArgs() {
            return args;
        }

        public Set<String> getFlags() {
            return flags;
        }

        public boolean has(String flag) {
            return flags.contains(flag);
        }

        public Map<String, String> getValues() {
            return values;
        }

        public String get(int i) {
            return args.get(i);
        }

        public String get(String flag) {
            return values.get(flag);
        }

        public boolean getBool(String flag, boolean def) {
            String val = get(flag);
            if (val == null) return def;
            return val == "true";
        }

        public int getInt(String flag, int def) {
            String val = get(flag);
            if (val == null) return def;
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public int length() {
            return args.size();
        }

    }

}