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
package com.google.gwt.cell.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;

/**
 * A {@link Cell} used to render a checkbox.
 *
 * <p>
 * Note: This class is new and its interface subject to change.
 * </p>
 */
public class CheckboxCell extends AbstractEditableCell<Boolean, Boolean> {

  private final boolean isSelectBox;

  /**
   * Construct a new {@link CheckboxCell}.
   */
  public CheckboxCell() {
    this(false);
  }

  /**
   * Construct a new {@link CheckboxCell} that optionally controls selection.
   *
   * @param isSelectBox true if the cell controls the selection state
   */
  public CheckboxCell(boolean isSelectBox) {
    super("change");
    this.isSelectBox = isSelectBox;
  }

  @Override
  public boolean dependsOnSelection() {
    return isSelectBox;
  }

  @Override
  public boolean handlesSelection() {
    return isSelectBox;
  }

  @Override
  public void onBrowserEvent(Element parent, Boolean value, Object key,
      NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
    String type = event.getType();
    if ("change".equals(type)) {
      InputElement input = parent.getFirstChild().cast();
      Boolean isChecked = input.isChecked();
      setViewData(key, isChecked);
      if (valueUpdater != null) {
        valueUpdater.update(isChecked);
      }
    }
  }

  @Override
  public void render(Boolean value, Object key, StringBuilder sb) {
    // Get the view data.
    Boolean viewData = getViewData(key);
    if (viewData != null && viewData.equals(value)) {
      clearViewData(key);
      viewData = null;
    }

    sb.append("<input type=\"checkbox\"");
    if (value != null) {
      boolean isChecked = (viewData != null) ? viewData : value;
      if (isChecked) {
        sb.append(" checked");
      }
    }
    sb.append("/>");
  }
}
