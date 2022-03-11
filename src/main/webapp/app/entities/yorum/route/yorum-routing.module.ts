import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { YorumComponent } from '../list/yorum.component';
import { YorumDetailComponent } from '../detail/yorum-detail.component';
import { YorumUpdateComponent } from '../update/yorum-update.component';
import { YorumRoutingResolveService } from './yorum-routing-resolve.service';

const yorumRoute: Routes = [
  {
    path: '',
    component: YorumComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: YorumDetailComponent,
    resolve: {
      yorum: YorumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: YorumUpdateComponent,
    resolve: {
      yorum: YorumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: YorumUpdateComponent,
    resolve: {
      yorum: YorumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(yorumRoute)],
  exports: [RouterModule],
})
export class YorumRoutingModule {}
