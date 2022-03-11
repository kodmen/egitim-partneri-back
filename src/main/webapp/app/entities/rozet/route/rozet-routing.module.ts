import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RozetComponent } from '../list/rozet.component';
import { RozetDetailComponent } from '../detail/rozet-detail.component';
import { RozetUpdateComponent } from '../update/rozet-update.component';
import { RozetRoutingResolveService } from './rozet-routing-resolve.service';

const rozetRoute: Routes = [
  {
    path: '',
    component: RozetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RozetDetailComponent,
    resolve: {
      rozet: RozetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RozetUpdateComponent,
    resolve: {
      rozet: RozetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RozetUpdateComponent,
    resolve: {
      rozet: RozetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rozetRoute)],
  exports: [RouterModule],
})
export class RozetRoutingModule {}
