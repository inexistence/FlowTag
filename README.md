# FlowTagsLayout

Flow Tags

##Usage

Use it as ListView

###Example XML

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.jianbin.flowtag.MainActivity" >

    <com.jianbin.view.FlowTagsLayout
        android:id="@+id/fl_tags"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:clickable="true" />

</RelativeLayout>
```

###Example Java

Initial

```java
mAdapter = new ArrayAdapter<String>(this, R.layout.tags_item,
				R.id.btn_tags_item_text, new String[]{"TAG1","TAG2","TAG2","TAG3"});
mFlowTagsLayout.setAdapter(mAdapter);
```

Add new tag

```java
mAdapter.add("TAG4");
```

set item click listener

```java
mFlowTagsLayout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						"you click tag " + mAdapter.getItem(position),
						Toast.LENGTH_SHORT).show();
			}

		});
```

set item long click listener

```java
mFlowTagsLayout.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Toast.makeText(MainActivity.this,
								"long click tag " + mAdapter.getItem(position),
								Toast.LENGTH_SHORT).show();
						return true;
					}

				});
```