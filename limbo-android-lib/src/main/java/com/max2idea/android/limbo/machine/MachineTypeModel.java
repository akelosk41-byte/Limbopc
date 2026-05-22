/*
Copyright (C) Max Kastanas 2012

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.max2idea.android.limbo.machine;

/**
 * A machine-type entry holding both a human-friendly display name (rendered in
 * the UI) and the raw QEMU machine identifier that is actually passed to QEMU
 * via the -M argument and persisted to the machine database.
 *
 * Raw lines in the architecture *_machine_types.txt resources use the format
 *     Display Name|qemu-machine-id
 * Lines without a '|' separator use the same value for both fields, preserving
 * backward compatibility with the older single-column format.
 */
public final class MachineTypeModel {
    public static final String SEPARATOR = "|";

    private final String displayName;
    private final String qemuId;

    public MachineTypeModel(String displayName, String qemuId) {
        this.displayName = displayName;
        this.qemuId = qemuId;
    }

    public static MachineTypeModel parse(String rawLine) {
        if (rawLine == null) return null;
        String line = rawLine.trim();
        if (line.isEmpty()) return null;
        int sep = line.indexOf(SEPARATOR);
        if (sep < 0) return new MachineTypeModel(line, line);
        String display = line.substring(0, sep).trim();
        String id = line.substring(sep + 1).trim();
        if (display.isEmpty()) display = id;
        if (id.isEmpty()) id = display;
        return new MachineTypeModel(display, id);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getQemuId() {
        return qemuId;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MachineTypeModel)) return false;
        MachineTypeModel other = (MachineTypeModel) o;
        return qemuId.equals(other.qemuId);
    }

    @Override
    public int hashCode() {
        return qemuId.hashCode();
    }
}
