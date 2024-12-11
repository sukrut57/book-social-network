import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.scss'
})
export class StarRatingComponent {

  @Input() rating: number | undefined ;

  get stars() {
    if(this.rating){
      return Array(Math.floor(this.rating)).fill(0);
    }
    return Array(5).fill(0);
  }
}
