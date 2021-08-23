# Sealed Class

> Enum과 상당히 유사하며, 개인적으로 판단하기에 sealed 와 enum을 선택하는 기준은 <br>
> 요소간 공통점이 많으면 enum 요소간 차이점이 많으면 sealed 선호

<br>

- ### DetailViewHolder의 예
    -  DetailActivity는 전체가 RecyclerView형태로 되어있음
    -  각 아이템은 Image를 담는 ImageViewHolder, Text 위주의 TextViewHolder, 지도를 가지는 MapViewHolder가 존재함 
    -  **DetailViewHolder**를 상속한다는 것 이외에는 큰 차이점이 없음으로 sealed 사용

```kotlin

sealed class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // val title : TextView =  view.findViewById<TextView>(R.id.detailItemTextTitle) 또는
    // abstract val title : TextView 와 같이 공통적인 변수나 메소드를 구체화 또는 추상화된 형태로 가질 수 있음.
    
    class DetailImageViewHolder(view: View) : DetailViewHolder(view) {
        val imageView: ImageView = view.findViewById<ImageView>(R.id.detailItemImage)
    }

    class DetailTextViewHolder(view: View) : DetailViewHolder(view) {
        val title: TextView = view.findViewById<TextView>(R.id.detailItemTextTitle)
        val contents: TextView = view.findViewById<TextView>(R.id.detailItemTextContent)
    }

    class DetailMapViewHolder(view: View) : DetailViewHolder(view), OnMapReadyCallback {
        val title: TextView = view.findViewById<TextView>(R.id.detailItemMapTitle)
        val mapView: MapView = view.findViewById<MapView>(R.id.detailItemMapView)

        private val zoomLevel = 16.5f
        private var latLng: LatLng? = null

        var map: GoogleMap? = null

        fun setUpLatLng(latLng: LatLng?) {
            this.latLng = latLng
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap

            map?.uiSettings?.isScrollGesturesEnabled = false

            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, zoomLevel
                )
            )

            map?.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
        }
    }
}
```

<br>

- ### 사용하는 부분에서는 Sealed Class 를 사용하였음으로 when문에서 else문을 작성하지 않아도 된다는 큰 장점이 생김 

```kotlin
override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
  viewModel.items[position].let { item ->
    when (holder) { // sealed class로 제한해두었기떄문에 else 문을 사용하지 않아도됨
      is DetailViewHolder.DetailImageViewHolder -> { 
        Glide
          .with(viewModel.context)
          .load(item.parseData<DetailParsedType.StringParsedType>(viewModel.data).item)
          .into(holder.imageView)
      }
      is DetailViewHolder.DetailMapViewHolder -> {
        holder.title.text = resString(item.title!!)
        holder.setUpLatLng(item.parseData<DetailParsedType.LatLngParsedType>(viewModel.data).item)
        holder.mapView.getMapAsync(holder)
        MapLifeCycle(viewModel.lifecycle, holder.mapView)
      }
      is DetailViewHolder.DetailTextViewHolder -> {
        holder.title.text = resString(item.title!!)
        when(item){
          DetailItemType.TAG -> {
            holder.contents.text = resString(item.parseData<DetailParsedType.IntParsedType>(viewModel.data).item)
          }
          DetailItemType.TITLE, DetailItemType.DESCRIPTION ,DetailItemType.FULL_ADDRESS -> {
            holder.contents.text = item.parseData<DetailParsedType.StringParsedType>(viewModel.data).item
          }
          else -> {}
        }
      }
    }
  }
}
```

<br> 

- ### DetailParsedType의 예
    - ContentsRetreiveResponse에서 RecyclerView에 맞게 추출하는 예
    - 문제는 추출되는 타입형이 Int, String, LatLng 세가지 타입이 존재
    - 타입 스크립트에서는 Union 타입을 활용해 편하게 구현할 수 있지만, 코틀린은 미지원
    - Sealed Class를 사용하여 각 아이템을 가질 수 있는 클래스 제한되게 생성하여 Union과 비슷하게 사용
    - 일반적인 class로도 가능하지만, sealed class는 해당 클래스 블록 내에서만 정의가 가능하여 의도치 않은 에러를 줄일 수 있음.

```kotlin

enum class DetailItemType(
    val title: Int?,
    val viewType: DetailAdapterViewType,
    val parsedType: Class<out DetailParsedType>
) {
    IMAGE(
        null,
        DetailAdapterViewType.IMAGE,
        DetailParsedType.StringParsedType::class.java
    )
    /*...*/
    ;
 
    @Throws(RuntimeException::class)
    inline fun <reified CASTED_CLASS : DetailParsedType> parseData(
        data: ContentsRetrieveResponse,
    ): CASTED_CLASS {
        if (this.parsedType != CASTED_CLASS::class.java) throw RuntimeException("지원하지 않는 타입")
        return when (this) {
            IMAGE -> DetailParsedType.StringParsedType(data.picture)
            TITLE -> DetailParsedType.StringParsedType(data.title)
            DESCRIPTION -> DetailParsedType.StringParsedType(data.description)
            TAG -> DetailParsedType.IntParsedType(data.tag.kor)
            FULL_ADDRESS -> DetailParsedType.StringParsedType(data.fullAddress)
            MAP -> DetailParsedType.LatLngParsedType(LatLng(data.lat, data.lng))
        } as CASTED_CLASS
    }
}

// 조회된 데이터에서 파싱될 데이터 타입(String, Int, LatLng)을 정의
sealed class DetailParsedType {
    class StringParsedType(val item: String) : DetailParsedType()
    class IntParsedType(val item: Int) : DetailParsedType()
    class LatLngParsedType(val item: LatLng) : DetailParsedType()
}
```