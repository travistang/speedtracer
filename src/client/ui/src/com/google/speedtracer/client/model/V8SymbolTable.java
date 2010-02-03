/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.speedtracer.client.model;

import java.util.TreeMap;

/**
 * Class used to hold an address to symbol map for V8 Profile data.
 */
public class V8SymbolTable {
  /**
   * A single block of code occupies a span of address space represented by this
   * data structure.
   */
  static class AddressSpan implements Comparable<AddressSpan> {
    int addressLength;
    double address;

    public AddressSpan(double address, int addressLength) {
      this.address = address;
      this.addressLength = addressLength;
    }

    /**
     * This comparison function doesn't search for exact equality. Any overlap
     * between the two address spans is considered a match.
     */
    public int compareTo(AddressSpan compareAddress) {

      double aStart = compareAddress.address;
      double aEnd = aStart + compareAddress.addressLength;
      double bStart = address;
      double bEnd = bStart + addressLength;

      if (bStart >= aStart && bStart <= aEnd) {
        return 0;
      }
      if (aStart >= bStart && aStart <= bEnd) {
        return 0;
      }
      if (aStart < bStart) {
        return -1;
      }
      return 1;
    }

    public double getAddress() {
      return address;
    }

    public int getLength() {
      return addressLength;
    }

    public String toString() {
      return "0x" + Long.toHexString((long) address) + "-0x"
          + Long.toHexString((long) address + addressLength);
    }
  }

  /**
   * Stores a address used as context to decompress address fields from the log.
   */
  static class AddressTag {
    public final String name;
    public double prevAddress = 0;

    AddressTag(String name) {
      this.name = name;
    }

    public double get() {
      return prevAddress;
    }
  }

  /**
   * code-creation entries in the log create these symbols to be used to lookup
   * program counter entries in the tick data.
   */
  static class Symbol {
    private final AddressSpan addressSpan;
    private final String name;
    private final int symbolType;

    Symbol(String name, int symbolType, double address, int addressLength) {
      this.name = name;
      this.symbolType = symbolType;
      this.addressSpan = new AddressSpan(address, addressLength);
    }

    public AddressSpan getAddressSpan() {
      return this.addressSpan;
    }

    public String getName() {
      return this.name;
    }

    public int getSymbolType() {
      return this.symbolType;
    }

    public String toString() {
      return name + " : " + addressSpan.toString();
    }
  }

  private TreeMap<AddressSpan, Symbol> table = new TreeMap<AddressSpan, Symbol>();

  public V8SymbolTable() {
  }

  /**
   * Add a symbol to the table.
   * 
   * Note collisions overwrite the previous value.
   */
  public void add(Symbol toAdd) {
    table.put(toAdd.getAddressSpan(), toAdd);
  }

  /**
   * A dump of the data stored in this symbol table intended only for debugging.
   */
  public void debugDumpHtml(StringBuilder output) {
    output.append("<table>");
    output.append("<tr><th>Name</th><th>Address</th><th>Length</th></tr>");
    for (Symbol child : table.values()) {
      output.append("<tr>");
      output.append("<td>" + child.getName() + "</td>");
      output.append("<td>" + Long.toHexString((long) child.getAddressSpan().getAddress()) + "</td>");
      output.append("<td>" + Long.toString(child.getAddressSpan().addressLength) + "</td>");
      output.append("</tr>");
    }
    output.append("</table>");
  }

  public Symbol lookup(double address) {
    return table.get(new AddressSpan(address, 0));
  }

  public void remove(Symbol toRemove) {
    table.remove(toRemove.getAddressSpan());
  }
}