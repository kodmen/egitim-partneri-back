import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImageModelComponent } from '../list/image-model.component';
import { ImageModelDetailComponent } from '../detail/image-model-detail.component';
import { ImageModelUpdateComponent } from '../update/image-model-update.component';
import { ImageModelRoutingResolveService } from './image-model-routing-resolve.service';

const imageModelRoute: Routes = [
  {
    path: '',
    component: ImageModelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImageModelDetailComponent,
    resolve: {
      imageModel: ImageModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImageModelUpdateComponent,
    resolve: {
      imageModel: ImageModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImageModelUpdateComponent,
    resolve: {
      imageModel: ImageModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageModelRoute)],
  exports: [RouterModule],
})
export class ImageModelRoutingModule {}
