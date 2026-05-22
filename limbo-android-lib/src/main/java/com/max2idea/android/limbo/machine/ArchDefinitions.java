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

import android.content.Context;

import com.limbo.emu.lib.R;
import com.max2idea.android.limbo.install.Installer;
import com.max2idea.android.limbo.main.Config;
import com.max2idea.android.limbo.main.LimboApplication;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple utility class to retrieved often long architecture attribute lists
 */
public class ArchDefinitions {
    private static String TAG = "ArchDefinitions";

    public static ArrayList<String> getSoundcards(Context context) {
        ArrayList<String> commonSoundcards = new ArrayList<>();
        commonSoundcards.addAll(Arrays.asList(Installer.getAttrs(context, R.raw.common_soundcards)));
        return commonSoundcards;
    }

    public static ArrayList<String> getNetworkDevices(Context context) {
        ArrayList<String> commonNetworkCards = new ArrayList<>();
        commonNetworkCards.addAll(Arrays.asList(Installer.getAttrs(context, R.raw.common_nic_cards)));

        ArrayList<String> networkCards = new ArrayList<>();
        switch (LimboApplication.arch) {
            case x86:
            case x86_64:
                networkCards.add("Default");
                networkCards.addAll(commonNetworkCards);
                break;
            case arm:
            case arm64:
                networkCards.add("Default");
                networkCards.addAll(commonNetworkCards);
                networkCards.addAll(Arrays.asList(Installer.getAttrs(context, R.raw.arm_nic_cards)));
                break;
            case ppc:
            case ppc64:
                networkCards.add("Default");
                networkCards.addAll(commonNetworkCards);
                break;
            case sparc:
            case sparc64:
                networkCards.add("Default");
                networkCards.addAll(Arrays.asList(Installer.getAttrs(context, R.raw.sparc_nic_cards)));
                break;
        }
        return networkCards;
    }

    public static ArrayList<String> getVGAValues(Context context) {
        ArrayList<String> vgaValues = new ArrayList<>();
        if (LimboApplication.arch == Config.Arch.x86 || LimboApplication.arch == Config.Arch.x86_64
                || LimboApplication.arch == Config.Arch.arm || LimboApplication.arch == Config.Arch.arm64
                || LimboApplication.arch == Config.Arch.ppc || LimboApplication.arch == Config.Arch.ppc64) {
            vgaValues.add("std");
        }

        if (LimboApplication.arch == Config.Arch.x86 || LimboApplication.arch == Config.Arch.x86_64) {
            vgaValues.add("cirrus");
            vgaValues.add("vmware");
        }

        //override vga for sun4m (32bit) machines to cg3
        if (LimboApplication.arch == Config.Arch.sparc || LimboApplication.arch == Config.Arch.sparc64) {
            vgaValues.add("cg3");
        }

        if (LimboApplication.arch == Config.Arch.arm || LimboApplication.arch == Config.Arch.arm64) {
            vgaValues.add("virtio-gpu-pci");
        }

        //XXX: some archs don't support vga on QEMU like SPARC64
        vgaValues.add("nographic");

        //TODO: Add XEN???
        // "xenfb"
        return vgaValues;
    }

    public static ArrayList<String> getKeyboardValues(Context context) {
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("en-us");
        return arrList;
    }

    public static ArrayList<String> getMouseValues(Context context) {
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("ps2");
        arrList.add("usb-mouse");
        arrList.add("usb-tablet" + " " + context.getString(R.string.fixesMouseParen));
        return arrList;
    }

    public static ArrayList<String> getUIValues() {
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("VNC");
        if (Config.enable_SDL)
            arrList.add("SDL");
        return arrList;
    }

    public static ArrayList<String> getMachineValues(Context context) {
        ArrayList<String> machinesList = new ArrayList<>();
        machinesList.add("None");
        machinesList.add("New");
        return machinesList;
    }

    public static ArrayList<CpuModel> getCpuValues(Context context) {
        ArrayList<CpuModel> arrList = new ArrayList<>();
        arrList.add(new CpuModel("Default", "Default"));
        String[] raw = null;
        switch (LimboApplication.arch) {
            case x86:
            case x86_64:
                raw = Installer.getAttrs(context, R.raw.x86_cpu);
                break;
            case arm:
            case arm64:
                raw = Installer.getAttrs(context, R.raw.arm_cpu);
                break;
            case ppc:
            case ppc64:
                raw = Installer.getAttrs(context, R.raw.ppc_cpu);
                break;
            case sparc:
            case sparc64:
                raw = Installer.getAttrs(context, R.raw.arm_cpu);
                break;
        }
        if (raw != null) {
            for (String line : raw) {
                CpuModel cpu = CpuModel.parse(line);
                if (cpu != null) {
                    arrList.add(cpu);
                }
            }
        }

        if (LimboApplication.arch == Config.Arch.x86 || LimboApplication.arch == Config.Arch.x86_64
                || LimboApplication.arch == Config.Arch.arm || LimboApplication.arch == Config.Arch.arm64)
            arrList.add(new CpuModel("host", "host"));
        return arrList;
    }

    public static ArrayList<MachineTypeModel> getMachineTypeValues(Context context) {
        ArrayList<MachineTypeModel> arrList = new ArrayList<>();
        String[] raw = null;
        boolean prependDefault = true;
        switch (LimboApplication.arch) {
            case x86:
            case x86_64:
                raw = Installer.getAttrs(context, R.raw.x86_machine_types);
                break;
            case arm:
            case arm64:
                raw = Installer.getAttrs(context, R.raw.arm_machine_types);
                prependDefault = false;
                break;
            case ppc:
            case ppc64:
                raw = Installer.getAttrs(context, R.raw.ppc_machine_types);
                break;
            case sparc:
            case sparc64:
                raw = Installer.getAttrs(context, R.raw.sparc_machine_types);
                break;
        }
        if (prependDefault) {
            arrList.add(new MachineTypeModel("Default", "Default"));
        }
        if (raw != null) {
            for (String line : raw) {
                MachineTypeModel mt = MachineTypeModel.parse(line);
                if (mt != null) {
                    arrList.add(mt);
                }
            }
        }
        return arrList;
    }
}
